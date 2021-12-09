package com.youdao.ead.reptile.market;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.youdao.ead.common.util.HttpUtils;
import com.youdao.ead.reptile.market.constant.AppAnnieParam;
import com.youdao.ead.reptile.market.constant.TypeConstants;
import com.youdao.ead.reptile.market.db.entity.*;
import com.youdao.ead.reptile.market.db.mapper.AppAnnieCategoryMapper;
import com.youdao.ead.reptile.market.db.mapper.AppCountryInfoMapper;
import com.youdao.ead.reptile.market.db.mapper.AppInfoMapper;
import com.youdao.ead.reptile.market.dto.AppDetailDto;
import com.youdao.ead.reptile.market.dto.AppOverviewDto;
import com.youdao.ead.reptile.market.service.CookieService;
import lombok.NonNull;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.zookeeper.CreateMode;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 从 https://www.appannie.com/ 和 Google Play、App Store 抓取应用信息
 * 每天定时抓取，数据存入mysql+es
 *
 * @author huqj
 */
@Component
@PropertySource("classpath:appannie.properties")
@EnableScheduling
@Lazy(false)
public class AppAnnieCrawler2 {

    private static final Logger log = LoggerFactory.getLogger("appLog");

    @Resource
    private AppInfoMapper appInfoMapper;

    @Resource
    private AppCountryInfoMapper appCountryInfoMapper;

    @Resource
    private AppAnnieCategoryMapper appAnnieCategoryMapper;

    @Resource
    private GooglePlayCrawler googlePlayCrawler;

    @Resource
    private AppStoreCrawler appStoreCrawler;

    @Resource
    private AppMarketEsService appMarketEsService;

    @Resource
    private MetricRegistry metricRegistry;

    @Resource
    private CuratorFramework curatorFramework;

    @Resource
    private CookieService cookieService;

    @Value("${appannie.username}")
    private String appAnnieUsername;

    @Value("${appannie.password}")
    private String appAnniePassword;

    /**
     * 一个需要登录的AppAnnie页面
     */
    @Value("${appannie.homepage}")
    private String appAnnieHomePage;

    /**
     * 获取google play榜单的api
     */
    @Value("${appannie.rank.gp.api}")
    private String googlePlayRankApiUrl;

    /**
     * 获取app store榜单的api
     */
    @Value("${appannie.rank.appStore.api}")
    private String appStoreRankApiUrl;

    /**
     * 获取app详细信息的api
     */
    @Value("${appannie.app.detail.api}")
    private String appDetailApiUrl;

    /**
     * 获取app是否被推荐的api
     */
    @Value("${appannie.app.recommend.api}")
    private String appIsRecommendedApiUrl;

    /**
     * 获取最近更新时间的api
     */
    @Value("${appannie.app.lastUpdateTime.api}")
    private String lastUpdateTimeApiUrl;

    /**
     * 需要抓取的国家的代号
     */
    @Value("#{${appannie.country.code.map}}")
    private Map<String, String> countryCodeMap;

    /**
     * google play总榜抓取的条数
     */
    @Value("${appannie.rank.limit.all.gp}")
    private int googlePlayRankLimitAll;

    /**
     * google play分榜抓取的条数
     */
    @Value("${appannie.rank.limit.sub.gp}")
    private int googlePlayRankLimitSub;

    /**
     * app store总榜抓取的条数
     */
    @Value("${appannie.rank.limit.all.appStore}")
    private int appStoreRankLimitAll;

    /**
     * app store总榜抓取的条数
     */
    @Value("${appannie.rank.limit.sub.appStore}")
    private int appStoreRankLimitSub;

    @Value("${appannie.zk.distributed.lock.path}")
    private String pathForZkLock;

    @Value("${appannie.zk.update.path}")
    private String updatePathForZk;

    @Value("${appannie.zk.distributed.lock.acquireMaxWaitMs}")
    private long acquireMaxWaitMsForZkLock;

    private ChromeDriver driver = null;

    /**
     * 一次爬虫任务时间统计
     */
    private Timer appCrawlTimer;

    /**
     * 一个榜单抓取时间统计
     */
    private Timer rankCrawlTimer;

    /**
     * 单个页面抓取时间统计
     */
    private Timer appDetailCrawlTimer;

    private Meter errorMeter;

    /**
     * 隐式等待页面元素加载的最长时间
     */
    private static final long MAX_IMPLICIT_WAIT_SEC = 30;

    /**
     * 批量查询app是否被推荐时批量请求查询的app数量
     */
    private static final int APP_RECOMMENDED_BATCH_QUERY_LIMIT = 50;

    /**
     * chrome driver实例数，比较占cpu和内存，不能开太多
     */
    private static final int CHROME_DRIVER_MAX_INSTANCE_NUM = 20;

    /**
     * 登录页面的标识元素
     */
    private static final String USERNAME_INPUT_FLAG = "placeholder=\"Email\"";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * app annie中 productId 到 包名 的映射关系的缓存
     */
    private static final Cache<Long, String> PRODUCT_TO_PACKAGE_NAME_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(3, TimeUnit.DAYS)
            .build();

    /**
     * app annie中 productId+国家代码 到 是否被首页推荐 的映射关系的缓存
     */
    private static final Cache<String, Boolean> PRODUCT_TO_RECOMMENDED_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(3, TimeUnit.HOURS)
            .build();

    /**
     * 用于多线程抓取应用官网详情页的线程池
     */
    private static final ExecutorService FIX_DRIVER_THREAD_POOL = new ThreadPoolExecutor(
            CHROME_DRIVER_MAX_INSTANCE_NUM,
            CHROME_DRIVER_MAX_INSTANCE_NUM,
            2,
            TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(2048),
            new ThreadFactoryBuilder()
                    .setNameFormat("app-annie-thread-%d")
                    .setDaemon(false)
                    .build()
    );

    private static final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(
            1,
            new ThreadFactoryBuilder()
                    .setNameFormat("app-crawler-thread-%d")
                    .setDaemon(false)
                    .build()
    );

    private static final Map<String, String> countryCode2LanguageHeader = new HashMap<String, String>() {
        {
            this.put("JP", "aa_language=jp;django_language=ja;");
            this.put("US", "aa_language=en;django_language=en;");
            this.put("GB", "aa_language=en;django_language=en;");
            this.put("BR", "aa_language=en;django_language=en;");
            this.put("IN", "aa_language=en;django_language=en;");
            this.put("AU", "aa_language=en;django_language=en;");
            this.put("KR", "aa_language=kr;django_language=ko;");
            this.put("FR", "aa_language=fr;django_language=fr;");
            this.put("DE", "aa_language=de;django_language=de;");
        }
    };

    @PreDestroy
    private void shutdown() {
        FIX_DRIVER_THREAD_POOL.shutdown();
        googlePlayCrawler.quit();
        appStoreCrawler.quit();
    }

    @PostConstruct
    public void startJob() {
        appCrawlTimer = metricRegistry.timer("app-crawler-job-timer");
        rankCrawlTimer = metricRegistry.timer("app-crawler-timer");
        appDetailCrawlTimer = metricRegistry.timer("app-detail-crawler-timer");
        errorMeter = metricRegistry.meter("app-crawler-error");
        executorService.scheduleWithFixedDelay(this::startCrawler, 0, 1, TimeUnit.MINUTES);
    }

    private void startCrawler() {
        try (Timer.Context ignore = appCrawlTimer.time()) {
            log.info("start app market crawler...");
            String cookie = cookieService.getCookie();
            cookieService.releaseDriver();
            log.info("get cookie: {}", cookie);
            //抓取google play
            log.info("start google play crawler.");
            startGooglePlayCrawler(cookie);
            googlePlayCrawler.quit();
            log.info("finish google play crawler.");
            //抓取app store
            log.info("start app store crawler.");
            startAppStoreCrawler(cookie);
            appStoreCrawler.quit();
            log.info("finish app store crawler.");
        } catch (Exception e) {
            errorMeter.mark();
            log.error("crawl app annie error.", e);
        } finally {
            cookieService.releaseDriver();
        }
    }

    /**
     * 抓取android应用数据并存储
     */
    private void startGooglePlayCrawler(String cookie) {
        List<AppAnnieCategory> categoryList = appAnnieCategoryMapper.getByPlatform(TypeConstants.PLATFORM_ANDROID);
        for (String countryCode : countryCodeMap.keySet()) {
            try {
                //同一个国家的榜单只能有一个实例抓取
                String lockPathForCountry = pathForZkLock + "/android/" + countryCode;
                InterProcessLock distributionLock = new InterProcessMutex(curatorFramework, lockPathForCountry);
                try {
                    //获取分布式锁
                    if (!distributionLock.acquire(acquireMaxWaitMsForZkLock, TimeUnit.MILLISECONDS)) {
                        log.warn("acquire distributed lock timeout in {} ms.", acquireMaxWaitMsForZkLock);
                        return;
                    }
                    log.info("get lock success at {}, lockPathInZk is {}", LocalDateTime.now(), lockPathForCountry);

                    //取当地的最近更新日期抓取
                    String zoneId = countryCodeMap.get(countryCode);
                    String date = getLastUpdateDateByCountryCode(countryCode, zoneId, TypeConstants.PLATFORM_ANDROID, cookie);
                    LocalDate lastDate = LocalDate.parse(date, DATE_FORMATTER);
                    //检查该国家是否是今天更新过，如果是不需要再更新
                    String lastUpdateDatePath = updatePathForZk + "/android/" + countryCode;
                    LocalDate lastUpdateDate = getLastUpdateDate(lastUpdateDatePath);
                    if (lastUpdateDate != null) {
                        if (lastUpdateDate.isEqual(lastDate) || lastUpdateDate.isAfter(lastDate)) {
                            log.info("country {} on android was updated for date {}", countryCode, date);
                            continue;
                        } else if (lastUpdateDate.plusDays(1).isBefore(lastDate)) {
                            //如果最后更新时间和当前时间之间不是连续的，则抓取最后更新时间的后一天
                            lastDate = lastUpdateDate.plusDays(1);
                            date = DATE_FORMATTER.format(lastDate);
                        }
                    }
                    int successCategoryNum = 0;
                    for (AppAnnieCategory category : categoryList) {
                        log.info("start crawl gp, country: {}, category: {}, date: {}", countryCode, category, date);
                        try (Timer.Context ignored = rankCrawlTimer.time()) {
                            List<AppOverviewDto> googlePlayRankList =
                                    getGooglePlayRankList(countryCode, date, category.getCategoryId(), category.getLevel(), cookie);
                            log.info("get google play rank list, size: {}", googlePlayRankList.size());
                            uploadAppInfo2MysqlAndEs(googlePlayRankList, countryCode, category, date, googlePlayCrawler);
                            successCategoryNum++;
                        } catch (Exception e) {
                            errorMeter.mark();
                            log.error("error start google play crawler, country: {}, category: {}", countryCode, category.getCategoryId(), e);
                        }
                        log.info("end crawl gp.");
                    }
                    //至少需要有一个榜单抓取成功才设置当天抓取完成
                    if (successCategoryNum > 0) {
                        setUpdateDate(lastUpdateDatePath, date);
                    }
                } finally {
                    distributionLock.release();
                    log.info("release lock at {}, lockPathInZk is {}", LocalDateTime.now(), lockPathForCountry);
                }
            } catch (Exception e) {
                errorMeter.mark();
                log.error("error crawl for country: {}", countryCode, e);
            }
        }
    }

    /**
     * 抓取ios应用数据并存储
     */
    private void startAppStoreCrawler(String cookie) {
        List<AppAnnieCategory> categoryList = appAnnieCategoryMapper.getByPlatform(TypeConstants.PLATFORM_IOS);
        for (String countryCode : countryCodeMap.keySet()) {
            try {
                //同一个国家的榜单只能有一个实例抓取
                String lockPathForCountry = pathForZkLock + "/ios/" + countryCode;
                InterProcessLock distributionLock = new InterProcessMutex(curatorFramework, lockPathForCountry);
                try {
                    //获取分布式锁
                    if (!distributionLock.acquire(acquireMaxWaitMsForZkLock, TimeUnit.MILLISECONDS)) {
                        log.warn("acquire distributed lock timeout in {} ms.", acquireMaxWaitMsForZkLock);
                        return;
                    }
                    log.info("get lock success at {}, lockPathInZk is {}", LocalDateTime.now(), lockPathForCountry);
                    String zoneId = countryCodeMap.get(countryCode);
                    String date = getLastUpdateDateByCountryCode(countryCode, zoneId, TypeConstants.PLATFORM_IOS, cookie);
                    LocalDate lastDate = LocalDate.parse(date, DATE_FORMATTER);
                    //检查该国家是否是今天更新过，如果是不需要再更新
                    String lastUpdateDatePath = updatePathForZk + "/ios/" + countryCode;
                    LocalDate lastUpdateDate = getLastUpdateDate(lastUpdateDatePath);
                    if (lastUpdateDate != null) {
                        if (lastUpdateDate.isEqual(lastDate) || lastUpdateDate.isAfter(lastDate)) {
                            log.info("country {} on ios was updated for date {}", countryCode, date);
                            continue;
                        } else if (lastUpdateDate.plusDays(1).isBefore(lastDate)) {
                            //如果最后更新时间和当前时间之间不是连续的，则抓取最后更新时间的后一天
                            lastDate = lastUpdateDate.plusDays(1);
                            date = DATE_FORMATTER.format(lastDate);
                        }
                    }
                    int successCategoryNum = 0;
                    for (AppAnnieCategory category : categoryList) {
                        log.info("start crawl app store, country: {}, category: {}, date: {}", countryCode, category, date);
                        try (Timer.Context ignored = rankCrawlTimer.time()) {
                            List<AppOverviewDto> appStoreRankList =
                                    getAppStoreRankList(countryCode, date, category.getCategoryId(), category.getLevel(), category.getAppType(), cookie);
                            log.info("get app store rank list, size: {}", appStoreRankList.size());
                            uploadAppInfo2MysqlAndEs(appStoreRankList, countryCode, category, date, appStoreCrawler);
                            successCategoryNum++;
                        } catch (Exception e) {
                            errorMeter.mark();
                            log.error("error start app store crawler, country: {}, category: {}", countryCode, category.getCategoryId(), e);
                        }
                        log.info("end crawl app store.");
                    }
                    //至少需要有一个榜单抓取成功才设置当天抓取完成
                    if (successCategoryNum > 0) {
                        setUpdateDate(lastUpdateDatePath, date);
                    }
                } finally {
                    distributionLock.release();
                    log.info("release lock at {}, lockPathInZk is {}", LocalDateTime.now(), lockPathForCountry);
                }
            } catch (Exception e) {
                errorMeter.mark();
                log.error("error crawl for country: {}", countryCode, e);
            }
        }
    }

    private LocalDate getLastUpdateDate(String zkPath) throws Exception {
        if (curatorFramework.checkExists().forPath(zkPath) == null) {
            return null;
        }
        String lastUpdateDate = new String(curatorFramework.getData().forPath(zkPath));
        return LocalDate.parse(lastUpdateDate, DATE_FORMATTER);
    }

    private void setUpdateDate(String zkPath, String today) throws Exception {
        if (curatorFramework.checkExists().forPath(zkPath) == null) {
            curatorFramework.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(zkPath, today.getBytes());
            return;
        }
        curatorFramework.setData().forPath(zkPath, today.getBytes());
    }

    /**
     * 上报抓取的app信息到数据库和es
     */
    private void uploadAppInfo2MysqlAndEs(List<AppOverviewDto> appRankList, String countryCode,
                                          AppAnnieCategory category, String today, AppMarketCrawler crawler) throws InterruptedException {
        //查询数据库中已有app信息
        Map<String, AppInfo> appId2AppInfo = getAppInfoFromDbByIds(appRankList);
        //查询app在该国家的信息
        Map<String, AppCountryInfo> appCountryInfoMap = getAppCountryInfoFromDb(countryCode, appRankList);
        //并发抓取应用详情
        Map<String, AppDetailDto> appId2DetailMap = getAppDetailConcurrently(appRankList, appId2AppInfo, crawler);
        //写入数据库和es
        List<AppRankHistory> appRankHistoryList = new ArrayList<>(appRankList.size());
        List<AppInfoHistory> appInfoHistoryList = new ArrayList<>(appRankList.size());
        for (AppOverviewDto appOverviewDto : appRankList) {
            try {
                AppInfo appInfo = appId2AppInfo.get(appOverviewDto.getAppId());
                //app排行信息
                AppRankHistory appRankHistory = buildAppRankHistory(countryCode, buildRankListName(countryCode, category),
                        (category.getLevel() == TypeConstants.CATEGORY_LEVEL_1 ? TypeConstants.RANK_LIST_TYPE_ALL : TypeConstants.RANK_LIST_TYPE_SUB),
                        today, appOverviewDto);
                appRankHistoryList.add(appRankHistory);
                if (appInfo == null || appInfo.getLastUpdateDate().compareTo(LocalDate.now()) != 0) {
                    AppDetailDto appDetailInfo = appId2DetailMap.get(appOverviewDto.getAppId());
                    if (appDetailInfo != null) {
                        //今天没有更新过信息快照则向ES发消息并更新mysql数据
                        AppInfoHistory appInfoHistory = buildAppInfoHistory(today, appDetailInfo);
                        appInfoHistoryList.add(appInfoHistory);
                        if (appInfo == null) {
                            //app之前在库里没有，需要入库
                            appInfoMapper.insert(buildAppInfo(appDetailInfo, category.getPlatform(), category.getAppType()));
                            log.info("insert new app, appId: {}", appDetailInfo.getAppId());
                        } else {
                            //库里有但是今天没有更新过，则更新app信息
                            appInfo.setAppName(appDetailInfo.getAppName());
                            appInfo.setSubTitle(appDetailInfo.getSubTitle());
                            appInfo.setCategory(appDetailInfo.getCategory());
                            appInfo.setProvider(appDetailInfo.getProvider());
                            appInfo.setProviderEmail(appDetailInfo.getProviderEmail());
                            appInfo.setProviderOfficialWebsite(appDetailInfo.getProviderOfficialWebsite());
                            appInfo.setIconUrl(appDetailInfo.getIconUrl());
                            appInfo.setLastUpdateDate(LocalDate.now());
                            appInfoMapper.updateById(appInfo);
                        }
                    } else {
                        log.warn("app overview has no detail info: {}", appOverviewDto);
                    }
                }
                //app在当地的信息，例如当地语言表示的名称
                AppCountryInfo appCountryInfo = appCountryInfoMap.get(appOverviewDto.getAppId());
                if (appCountryInfo == null) {
                    appCountryInfo = AppCountryInfo.builder()
                            .appId(appOverviewDto.getAppId())
                            .appName(appOverviewDto.getAppName())
                            .countryCode(countryCode)
                            .platform(category.getPlatform())
                            .lastModTime(LocalDateTime.now())
                            .build();
                    appCountryInfoMapper.insert(appCountryInfo);
                } else {
                    if (!appCountryInfo.getAppName().equals(appOverviewDto.getAppName())) {
                        appCountryInfo.setAppName(appOverviewDto.getAppName());
                        appCountryInfoMapper.updateById(appCountryInfo);
                    }
                }
            } catch (Exception e) {
                errorMeter.mark();
                log.error("error upload app info: {}", appOverviewDto, e);
            }
        }
        //批量发送es插入请求
        appMarketEsService.sendAppRankHistory2Es(appRankHistoryList);
        appMarketEsService.sendAppInfoHistory2Es(appInfoHistoryList);
        log.info("finish send info to ES, appRankHistoryList size: {}, appInfoHistoryList size: {}",
                appRankHistoryList.size(), appInfoHistoryList.size());
    }

    private Map<String, AppDetailDto> getAppDetailConcurrently(List<AppOverviewDto> appRankList, Map<String, AppInfo> appId2AppInfo,
                                                               AppMarketCrawler crawler) throws InterruptedException {
        ConcurrentHashMap<String, AppDetailDto> appId2DetailMap = new ConcurrentHashMap<>(appRankList.size());
        CountDownLatch latch = new CountDownLatch(appRankList.size());
        for (AppOverviewDto appOverviewDto : appRankList) {
            AppInfo appInfo = appId2AppInfo.get(appOverviewDto.getAppId());
            //今天没有更新过的才去爬取详情页面
            if (appInfo == null || appInfo.getLastUpdateDate().compareTo(LocalDate.now()) != 0) {
                FIX_DRIVER_THREAD_POOL.submit(() -> {
                    try (Timer.Context ignore = appDetailCrawlTimer.time()) {
                        AppDetailDto appDetailInfo = crawler.getAppDetailInfoByAppId(appOverviewDto);
                        appId2DetailMap.put(appDetailInfo.getAppId(), appDetailInfo);
                    } catch (Exception e) {
                        errorMeter.mark();
                        log.error("error get app detail info.", e);
                    } finally {
                        latch.countDown();
                    }
                });
            } else {
                latch.countDown();
            }
        }
        latch.await();
        return appId2DetailMap;
    }

    private Map<String, AppInfo> getAppInfoFromDbByIds(List<AppOverviewDto> appRankList) {
        List<AppInfo> appInfoListInDb = appInfoMapper.getByAppIdList(
                appRankList.stream().map(AppOverviewDto::getAppId).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(appInfoListInDb)) {
            return Collections.emptyMap();
        }
        return appInfoListInDb.stream()
                .collect(Collectors.toMap(AppInfo::getAppId, Function.identity()));
    }

    private Map<String, AppCountryInfo> getAppCountryInfoFromDb(String countryCode, List<AppOverviewDto> appRankList) {
        List<AppCountryInfo> appCountryInfoList = appCountryInfoMapper.getByCountryAndAppIds(countryCode,
                appRankList.stream().map(AppOverviewDto::getAppId).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(appCountryInfoList)) {
            return Collections.emptyMap();
        }
        return appCountryInfoList.stream()
                .collect(Collectors.toMap(AppCountryInfo::getAppId, Function.identity()));
    }

    /**
     * 从appannie api抓取google play排行榜
     */
    private List<AppOverviewDto> getGooglePlayRankList(String countryCode, String date, long categoryId,
                                                       int categoryLevel, String cookie) throws Exception {
        String cookieExtraInfo = countryCode2LanguageHeader.getOrDefault(countryCode, "");
        HttpPost post = new HttpPost(googlePlayRankApiUrl);
        int limit = categoryLevel == TypeConstants.CATEGORY_LEVEL_1 ? googlePlayRankLimitAll : googlePlayRankLimitSub;
        post.setEntity(new StringEntity(String.format(AppAnnieParam.GP_RANK_API_PARAM_TEMPLATE, date, countryCode, categoryId, limit)));
        post.setHeader("Cookie", cookie + cookieExtraInfo);
        post.setHeader("Content-Type", "application/json");
        try (CloseableHttpResponse httpResponse = postAvoidExpiredCookie(post, cookie, cookieExtraInfo)) {
            String responseStr = EntityUtils.toString(httpResponse.getEntity());
            List<AppOverviewDto> appOverviewDtoList = parseAppListFromResponse(responseStr);
            //获取包名信息，该api有访问频次限制，只能单线程间隔着请求
            for (AppOverviewDto appOverviewDto : appOverviewDtoList) {
                try {
                    mergeAppPackageNameInfo(appOverviewDto, cookie, cookieExtraInfo);
                    Thread.sleep(100);
                } catch (Exception e) {
                    errorMeter.mark();
                    log.error("error get app package name info: {}", appOverviewDto, e);
                }
            }
            return appOverviewDtoList.stream().filter(app -> StringUtils.isNotEmpty(app.getAppId())).collect(Collectors.toList());
        }
    }

    /**
     * 从appannie api抓取app store排行榜
     */
    private List<AppOverviewDto> getAppStoreRankList(String countryCode, String date, long categoryId,
                                                     int categoryLevel, int appType, String cookie) throws Exception {
        String cookieExtraInfo = countryCode2LanguageHeader.getOrDefault(countryCode, "");
        HttpPost post = new HttpPost(appStoreRankApiUrl);
        int limit = categoryLevel == TypeConstants.CATEGORY_LEVEL_1 ? appStoreRankLimitAll : appStoreRankLimitSub;
        post.setEntity(new StringEntity(String.format(AppAnnieParam.APP_STORE_API_PARAM_TEMPLATE, date, countryCode, categoryId, limit)));
        post.setHeader("Cookie", cookie + cookieExtraInfo);
        post.setHeader("Content-Type", "application/json");
        try (CloseableHttpResponse httpResponse = postAvoidExpiredCookie(post, cookie, cookieExtraInfo)) {
            String responseStr = EntityUtils.toString(httpResponse.getEntity());
            List<AppOverviewDto> appOverviewDtoList = parseAppListFromResponse(responseStr);
            List<AppOverviewDto> appListWithoutRecommendInfo = new ArrayList<>(appOverviewDtoList.size());
            appOverviewDtoList.forEach(appOverviewDto -> {
                //ios应用的productId即为app store的appId
                appOverviewDto.setAppId(String.valueOf(appOverviewDto.getProductId()));
                Boolean appRecommend = PRODUCT_TO_RECOMMENDED_CACHE.getIfPresent(
                        buildAppAndCountryCodeKey(appOverviewDto.getProductId(), countryCode));
                if (appRecommend != null) {
                    appOverviewDto.setRecommended(appRecommend);
                } else {
                    appListWithoutRecommendInfo.add(appOverviewDto);
                }
            });
            //通过api获取该应用是否出现在首页推荐上，批量请求以减少请求次数
            List<List<AppOverviewDto>> partitions = Lists.partition(appListWithoutRecommendInfo, APP_RECOMMENDED_BATCH_QUERY_LIMIT);
            for (List<AppOverviewDto> subList : partitions) {
                try {
                    mergeIsRecommendedInfo(subList, appType, countryCode, date, cookie, cookieExtraInfo);
                    Thread.sleep(100);
                } catch (Exception e) {
                    errorMeter.mark();
                    log.error("error merge isRecommend info", e);
                }
            }
            return appOverviewDtoList.stream().filter(app -> StringUtils.isNotEmpty(app.getAppId())).collect(Collectors.toList());
        }
    }

    /**
     * 从http响应格式中解析需要的数据：app概要信息
     */
    private List<AppOverviewDto> parseAppListFromResponse(@NonNull String response) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(response);
        JSONObject data = jsonObject.getJSONObject("data");
        if (data != null) {
            //元数据
            JSONObject dimensions = data.getJSONObject("dimensions");
            if (dimensions != null) {
                //App类别
                JSONObject category = dimensions.getJSONObject("category_id");
                Map<String, String> categoryId2Name = new HashMap<>(16);
                if (category != null) {
                    categoryId2Name.putAll(category.keySet().stream()
                            .collect(Collectors.toMap(Function.identity(), k -> category.getJSONObject(k).getString("name"))));
                }
                //发行方
                JSONObject publisher = dimensions.getJSONObject("publisher_id");
                Map<String, String> publisherId2Name = new HashMap<>(16);
                if (publisher != null) {
                    publisherId2Name.putAll(publisher.keySet().stream()
                            .collect(Collectors.toMap(Function.identity(), k -> publisher.getJSONObject(k).getString("name"))));
                }
                //APP
                JSONObject products = dimensions.getJSONObject("product_id");
                Map<String, AppOverviewDto> productId2Info = new HashMap<>(16);
                if (products != null) {
                    productId2Info.putAll(products.keySet().stream()
                            .collect(Collectors.toMap(Function.identity(), k -> {
                                JSONObject product = products.getJSONObject(k);
                                return AppOverviewDto.builder()
                                        .appName(product.getString("name"))
                                        .productId(Long.parseLong(k))
                                        .category(categoryId2Name.getOrDefault(product.getString("category_id"), StringUtils.EMPTY))
                                        .publisher(publisherId2Name.getOrDefault(product.getString("publisher_id"), StringUtils.EMPTY))
                                        .build();
                            })));
                }
                //排行榜列表
                JSONArray facets = data.getJSONArray("facets");
                List<AppOverviewDto> appOverviewDtoList = new ArrayList<>(facets.size());
                for (int i = 0; i < facets.size(); i++) {
                    JSONObject facet = facets.getJSONObject(i);
                    AppOverviewDto overviewDto = productId2Info.get(facet.getString("product_id"));
                    if (overviewDto != null) {
                        Integer rankNo = facet.getInteger("store_product_rank_free__aggr");
                        overviewDto.setRankNo(rankNo);
                        appOverviewDtoList.add(overviewDto);
                    }
                }
                return appOverviewDtoList;
            } else {
                log.error("error parse app rank list, dimensions is null: {}", response);
                throw new Exception("response no data");
            }
        } else {
            log.error("error parse app rank list, data is null: {}", response);
            throw new Exception("response no data");
        }
    }

    /**
     * 从API查询榜上APP的包名
     */
    private void mergeAppPackageNameInfo(AppOverviewDto appOverviewDto, String cookie, String cookieExtraInfo) throws Exception {
        String packageName = PRODUCT_TO_PACKAGE_NAME_CACHE.getIfPresent(appOverviewDto.getProductId());
        if (StringUtils.isNotEmpty(packageName)) {
            appOverviewDto.setAppId(packageName);
            return;
        }
        HttpPost post = new HttpPost(appDetailApiUrl);
        post.setEntity(new StringEntity(String.format(AppAnnieParam.APP_PACKAGE_NAME_PARAM_TEMPLATE, appOverviewDto.getProductId())));
        post.setHeader("Cookie", cookie + cookieExtraInfo);
        post.setHeader("Content-Type", "application/json");
        try (CloseableHttpResponse httpResponse = postAvoidExpiredCookie(post, cookie, cookieExtraInfo)) {
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String responseStr = EntityUtils.toString(httpResponse.getEntity());
                if (StringUtils.isNotEmpty(responseStr)) {
                    JSONObject jsonObject = JSONObject.parseObject(responseStr);
                    JSONObject data = jsonObject.getJSONObject("data");
                    if (data != null) {
                        JSONArray facets = data.getJSONArray("facets");
                        if (facets.size() == 1) {
                            packageName = facets.getJSONObject(0).getString("package_name");
                            appOverviewDto.setAppId(packageName);
                            PRODUCT_TO_PACKAGE_NAME_CACHE.put(appOverviewDto.getProductId(), packageName);
                        } else {
                            log.error("get app package name error, facets size is not 1: {}, {}", appOverviewDto, responseStr);
                        }
                    } else {
                        log.error("get app package name error, data is null: {}, {}", appOverviewDto, responseStr);
                    }
                } else {
                    log.error("get app package name error, http response entity is empty: {}", appOverviewDto);
                }
            } else {
                errorMeter.mark();
                log.error("get app package name error, http status code {} is not OK : {}", httpResponse.getStatusLine().getStatusCode(), appOverviewDto);
            }
        }
    }

    /**
     * 从api查询应用是否被推荐的信息并将结果放入缓存
     */
    private void mergeIsRecommendedInfo(List<AppOverviewDto> appOverviewDto, int appType, String countryCode,
                                        String date, String cookie, String cookieExtraInfo) throws Exception {
        HttpPost post = new HttpPost(appIsRecommendedApiUrl);
        String featureType = appType == TypeConstants.APP_TYPE_APPLICATION ? TypeConstants.FEATURE_TYPE_APPS : TypeConstants.FEATURE_TYPE_GAMES;
        String productIdList = StringUtils.join(appOverviewDto.stream().map(AppOverviewDto::getProductId).collect(Collectors.toList()), ",");
        post.setEntity(new StringEntity(String.format(AppAnnieParam.APP_IS_RECOMMENDED_PARAM_TEMPLATE, date, featureType, countryCode, productIdList)));
        post.setHeader("Cookie", cookie + cookieExtraInfo);
        post.setHeader("Content-Type", "application/json");
        try (CloseableHttpResponse httpResponse = postAvoidExpiredCookie(post, cookie, cookieExtraInfo)) {
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String responseStr = EntityUtils.toString(httpResponse.getEntity());
                if (StringUtils.isNotEmpty(responseStr)) {
                    JSONObject jsonObject = JSONObject.parseObject(responseStr);
                    JSONObject data = jsonObject.getJSONObject("data");
                    if (data != null) {
                        Set<Long> recommendProductIds = new HashSet<>(appOverviewDto.size());
                        JSONArray facets = data.getJSONArray("facets");
                        if (facets != null && facets.size() > 0) {
                            JSONObject dimensions = data.getJSONObject("dimensions");
                            if (dimensions != null) {
                                JSONObject featuredItemId = dimensions.getJSONObject("featured_item_id");
                                Map<String, Long> featureId2ProductId = new HashMap<>(featuredItemId.size());
                                for (String featureId : featuredItemId.keySet()) {
                                    Long productId = featuredItemId.getJSONObject(featureId).getLong("product_id");
                                    featureId2ProductId.put(featureId, productId);
                                }
                                for (int i = 0; i < facets.size(); i++) {
                                    JSONObject facet = facets.getJSONObject(i);
                                    Long productId = featureId2ProductId.get(facet.getString("featured_item_id"));
                                    recommendProductIds.add(productId);
                                }
                            }
                        }
                        //给app列表元素设置是否被推荐属性，并将结果缓存
                        for (AppOverviewDto overviewDto : appOverviewDto) {
                            overviewDto.setRecommended(recommendProductIds.contains(overviewDto.getProductId()));
                            PRODUCT_TO_RECOMMENDED_CACHE.put(
                                    buildAppAndCountryCodeKey(overviewDto.getProductId(), countryCode),
                                    overviewDto.isRecommended());
                        }
                    } else {
                        log.error("get app recommend info error, data is null: {}, {}", appOverviewDto, responseStr);
                    }
                } else {
                    log.error("get app recommend info error, http response entity is empty: {}", appOverviewDto);
                }
            } else {
                errorMeter.mark();
                log.error("get app recommend info error, http status code {} is not OK : {}", httpResponse.getStatusLine().getStatusCode(), appOverviewDto);
            }
        }
    }

    /**
     * 从api获取某个国家某个平台榜单的最后更新时间，转成<b>当地时间</b>的yyyy-MM-dd形式
     */
    private String getLastUpdateDateByCountryCode(String countryCode, String zoneId, Integer platform, String cookie) {
        try {
            HttpPost post = new HttpPost(lastUpdateTimeApiUrl);
            String paramTemplate = platform == TypeConstants.PLATFORM_ANDROID ?
                    AppAnnieParam.LAST_UPDATE_TIME_PARAM_TEMPLATE_ANDROID : AppAnnieParam.LAST_UPDATE_TIME_PARAM_TEMPLATE_IOS;
            post.setEntity(new StringEntity(String.format(paramTemplate, countryCode)));
            post.setHeader("Cookie", cookie);
            post.setHeader("Content-Type", "application/json");
            try (CloseableHttpResponse httpResponse = postAvoidExpiredCookie(post, cookie)) {
                if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String responseStr = EntityUtils.toString(httpResponse.getEntity());
                    if (StringUtils.isNotEmpty(responseStr)) {
                        JSONObject jsonObject = JSONObject.parseObject(responseStr);
                        JSONObject data = jsonObject.getJSONObject("data");
                        if (data != null) {
                            JSONArray facets = data.getJSONArray("facets");
                            if (facets != null && facets.size() > 0) {
                                Long timestamp = facets.getJSONObject(0).getLong("store_rank_release_date_utc");
                                return DATE_FORMATTER.format(Instant.ofEpochMilli(timestamp * 1000L).atZone(ZoneId.of(zoneId)));
                            }
                        } else {
                            log.error("error get last update time, data is null.");
                        }
                    } else {
                        log.error("error get last update time, http response is empty.");
                    }
                } else {
                    errorMeter.mark();
                    log.error("error get last update time, http code {} is not OK.", httpResponse.getStatusLine().getStatusCode());
                }
            }
        } catch (Exception e) {
            errorMeter.mark();
            log.error("error get last update date by api.", e);
        }
        log.warn("get last update date by api fail, use default local date.");
        return LocalDate.now(ZoneId.of(zoneId)).format(DATE_FORMATTER);
    }

    private AppInfo buildAppInfo(AppDetailDto appDetailDto, Integer platform, Integer appType) {
        return AppInfo.builder()
                .appId(appDetailDto.getAppId())
                .appName(appDetailDto.getAppName())
                .subTitle(appDetailDto.getSubTitle())
                .detailUrl(appDetailDto.getDetailUrl())
                .iconUrl(appDetailDto.getIconUrl())
                .provider(appDetailDto.getProvider())
                .providerOfficialWebsite(appDetailDto.getProviderOfficialWebsite())
                .providerEmail(appDetailDto.getProviderEmail())
                .lastUpdateDate(LocalDate.now())
                .category(appDetailDto.getCategory())
                .appType(appType)
                .platform(platform)
                .lastModTime(LocalDateTime.now())
                .build();
    }

    private AppInfoHistory buildAppInfoHistory(String date, AppDetailDto appDetailDto) {
        return AppInfoHistory.builder()
                .appId(appDetailDto.getAppId())
                .appSize(appDetailDto.getAppSize())
                .appVersion(appDetailDto.getVersion())
                .date(date)
                .timestamp(System.currentTimeMillis())
                .updateDate(appDetailDto.getUpdateDate())
                .downloadNum(appDetailDto.getDownloadNum())
                .remarkNum(appDetailDto.getRemarkNum())
                .score(appDetailDto.getScore())
                .isRecommended(appDetailDto.isRecommended())
                .build();
    }

    private AppRankHistory buildAppRankHistory(String country, String listName, Integer listType, String date, AppOverviewDto appOverviewDto) {
        return AppRankHistory.builder()
                .appId(appOverviewDto.getAppId())
                .country(country)
                .listName(listName)
                .listType(listType)
                .ranking(appOverviewDto.getRankNo())
                .timestamp(System.currentTimeMillis())
                .date(date)
                .build();
    }

    private String buildRankListName(String countryCode, AppAnnieCategory category) {
        return "热门免费-" + countryCode + "-"
                + (category.getAppType() == TypeConstants.APP_TYPE_GAME ? "游戏" : "应用")
                + (category.getLevel() == TypeConstants.CATEGORY_LEVEL_2 ? "-" + category.getCategoryName() : "");
    }

    private String buildAppAndCountryCodeKey(Long productId, String countryCode) {
        return productId + "-" + countryCode;
    }

    private CloseableHttpResponse postAvoidExpiredCookie(HttpPost post, String cookie) throws Exception {
        return postAvoidExpiredCookie(post, cookie, "");
    }

    private CloseableHttpResponse postAvoidExpiredCookie(HttpPost post, String cookie, String cookieAddtionalInfo) throws Exception {
        try (CloseableHttpResponse httpResponse = HttpUtils.execute(post)) {
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return httpResponse;
            } else if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                cookie = cookieService.loginAndSetCookie();
                post.removeHeaders("Cookie");
                post.addHeader("Cookie", cookie + cookieAddtionalInfo);
                return HttpUtils.execute(post);
            } else {
                return httpResponse;
            }
        }
    }
}
