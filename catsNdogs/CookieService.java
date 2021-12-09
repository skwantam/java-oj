package com.youdao.ead.reptile.market.service;

import com.youdao.ead.common.util.ChromeDriverUtils;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CookieService {

    /**
     * 隐式等待页面元素加载的最长时间
     */
    private static final long MAX_IMPLICIT_WAIT_SEC = 30;
    /**
     * 登录页面的标识元素
     */
    private static final String USERNAME_INPUT_FLAG = "placeholder=\"Email\"";

    /**
     * 一个需要登录的AppAnnie页面
     */
    @Value("${appannie.homepage}")
    private String appAnnieHomePage;

    @Value("${appannie.username}")
    private String appAnnieUsername;

    @Value("${appannie.password}")
    private String appAnniePassword;

    private ChromeDriver driver = null;
    private String cookie = null;

    @PostConstruct
    private void init() {
        driver = ChromeDriverUtils.createProxy();
        driver.manage().timeouts().implicitlyWait(MAX_IMPLICIT_WAIT_SEC, TimeUnit.SECONDS);
        loginAndSetCookie();
    }

    @PreDestroy
    public void releaseDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    /**
     * 登录并设置cookie
     */
    public String loginAndSetCookie() {
        driver.get(appAnnieHomePage);
        if (driver.getPageSource().contains(USERNAME_INPUT_FLAG)) {
            WebElement emailInput = driver.findElementByXPath("//input[@placeholder='Email']");
            WebElement passwordInput = driver.findElementByXPath("//input[@placeholder='Password']");
            emailInput.sendKeys(appAnnieUsername);
            passwordInput.sendKeys(appAnniePassword);
            driver.findElementByXPath("//button[@type='submit']").click();
        }
        //这里为了等登陆响应所以去等待home页面的加载，然后再拿cookie
        driver.findElementsByXPath("//div[@class='Icon__StaticIcon-lcgvp9-0 iFPPRx']");
        Set<Cookie> cookies = driver.manage().getCookies();
        StringBuilder cookieStr = new StringBuilder();
        for (Cookie ck : cookies) {
            cookieStr.append(ck.getName())
                    .append("=")
                    .append(ck.getValue())
                    .append(";");
        }
        cookie = cookieStr.toString();
        return cookie;
    }

    public String getCookie() {
        return cookie;
    }
}
