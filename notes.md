[TOC]
## Dev Tools

### git
http://www.ruanyifeng.com/blog/2015/12/git-workflow.html (GitHub流程)
http://www.ruanyifeng.com/blog/2015/08/git-use-process.html
https://blog.carbonfive.com/always-squash-and-rebase-your-git-commits/
https://www.yiibai.com/git/git_pull.html
https://blog.csdn.net/u014643282/article/details/103598756

#### git Server
git init --bare sample.git

#### git Remote add
git remote add kfj ssh://tansj@zj035x.corp.youdao.com:4321/disk2/tansijun/gitServer/kolProj.git
git clone ssh://git@127.0.0.1:xx/yy.git(本地文件路径)

#### git 远程分支操作
https://blog.csdn.net/ljj_9/article/details/79386306
https://www.cnblogs.com/qyf404/p/git_push_local_branch_to_remote.html
https://www.cnblogs.com/luosongchao/p/3408365.html
https://blog.csdn.net/tterminator/article/details/52225720

### ssh
https://zhuanlan.zhihu.com/p/74193910

https://www.jianshu.com/p/b3dcd55ddc9d


### IDEA

#### debug
https://blog.csdn.net/fly910905/article/details/80013391

#### terminal
https://segmentfault.com/a/1190000012717033


## Java
https://blog.csdn.net/sinat_30160727/article/details/78434704

### lombok
https://projectlombok.org/api/

### Serialization
https://juejin.cn/post/7003220566698098695

### Maven
https://blog.csdn.net/zp357252539/article/details/80392101(mvn archetype:generate -DgroupId=com.tsj.oj -DartifactId=javaOj -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false)
https://blog.csdn.net/ibmeye/article/details/50705663 

### jUnit
https://www.jianshu.com/p/e72c5595710a

### Mybatis
https://blog.csdn.net/u010502101/article/details/79053980
https://blog.csdn.net/u010502101/article/details/79053980
https://blog.csdn.net/weixin_43822795/article/details/96587755
https://blog.csdn.net/erlian1992/article/details/78218977
https://blog.csdn.net/chengsi101/article/details/78804224
https://blog.csdn.net/l848168/article/details/91451534
https://blog.csdn.net/qq_36951116/article/details/85082716

## System Logic

### Cookie记录首次登录
https://www.jianshu.com/p/df53fb89da90
https://www.jianshu.com/p/5f13383b47de

### 账号绑定
http://blog.itpub.net/31557372/viewspace-2671003/


## Experience in Dev
https://blog.csdn.net/sinat_15155817/article/details/115214679


## System Design

### api design
https://zhuanlan.zhihu.com/p/56955812



## Network, Security and Protocol
https://zhuanlan.zhihu.com/p/147768058


## Mysql

### single table
ALTER TABLE `tbname`
	ADD COLUMN `state` TINYINT(2) NOT NULL DEFAULT '0' COMMENT '0为添加1为编辑' AFTER `column_name`;

https://blog.csdn.net/Pruett/article/details/72675005

### multi table
https://blog.csdn.net/helloxiaozhe/article/details/82261521

### 存储过程
```sql
delimiter $$
drop procedure if exists insert_english_name$$
create procedure insert_english_name()
begin 
	declare typeIds text;
   declare cooperationTypes text;
   declare quantities text;
	declare i int default 1;
	set typeIds = "1,2,3";
   set cooperationTypes = "Feed - Per Picture,Feed - Per Video,Story-Video/min";
   set quantities = "min,,";
   select length(typeIds) - length(REPLACE(typeIds, ',', '')) + 1 into @num;
   while i <= @num do
   		select substring_index(substring_index(typeIds,',',i),',', -1) into @uid;
      select substring_index(substring_index(cooperationTypes,',',i),',', -1) into @cts;
      select substring_index(substring_index(quantities,',',i),',', -1) into @qts;
      update student set coo=@cts, qua=@qts where student_id = @uid;
      # insert into student (student_name) values (@cts);
      set i = i+1;                        
	end while;
end$$
delimiter ;
call insert_english_name();
```

### foreign key constraint(不需要)
constraint fk_CooperationTypeUnit_CooperationType_On_ID
        foreign key (COOPERATION_TYPE_ID) 
		references CooperationType (ID)

### 游标Cursor
https://dev.mysql.com/doc/refman/8.0/en/cursors.html
```sql
delimiter $$
drop procedure if exists fillCooperatonTypeUnit$$
create procedure fillCooperatonTypeUnit()
begin
	declare done tinyint default false;
   declare cid int;
   declare str varchar(255) default '';
   declare cs cursor for (select id from test);
   declare continue handler for sqlstate '02000' set done=true;
   open cs;
   insert_loop:loop
   		fetch cs into cid;
      if done then
      		leave insert_loop;
    	end if;
      select d into str from test where id = cid; 
      insert into fun (h,k) values (cid, str);
   end loop;
   close cs;
end$$
delimiter ;
call fillCooperatonTypeUnit();
```


## Linux
https://linuxtools-rst.readthedocs.io/zh_CN/latest/index.html
https://zhongqi2513.blog.csdn.net/article/details/78613768

eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoia29sMTAwMDAiLCJpZCI6MTAwMDAsInR5cGUiOjEsImV4cCI6MTYzNzk1OTA4MTUxOSwiY3JlYXRlZCI6MTYzNzkyMzA4MTUxOX0.4cpv7zVggZcSVwgbOUBeHEp8-Pyhvy1aXYxMfBu1KF91yBWigT6e0lv1iz_b4seU9zcf4wFKZsvOHQBhLhJVOw
10000 kol10000

eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoic2VsbGVyMTAwNjM0IiwiaWQiOjEwMDAyLCJ0eXBlIjoxLCJleHAiOjE2Mzg4MDAzMzc4MDcsImNyZWF0ZWQiOjE2Mzg3NjQzMzc4MDd9.18fetYHku604qpxkuyS4WJ026lZuCOrkFLX8yH2dsuOASk8QeNwH5SFShDznY-TRemLqqa5SkHm_oomiWRuC5w
10002 kol10002


## OTHERS
https://wiki.jikexueyuan.com/list/redis/
http://localhost:11224/swagger-ui/index.html#/
https://matrix.corp.youdao.com/nginx/domain

```sql   
# 修改
update CooperationType set SERVICE_TYPE='素材授权' where COOPERATION_PLATFORM='Youtube' and SERVICE_TYPE='授权';
update CooperationType set SERVICE_TYPE='Feed-单图' where COOPERATION_PLATFORM='Instagram' and SERVICE_TYPE='Feed-图片';
update CooperationType set SERVICE_TYPE='Story-单图' where COOPERATION_PLATFORM='Instagram' and SERVICE_TYPE='Story-图片';
update CooperationType set SERVICE_TYPE='Feed-单图' where COOPERATION_PLATFORM='Facebook' and SERVICE_TYPE='Feed-图片';
update CooperationType set SERVICE_TYPE='Story-单图' where COOPERATION_PLATFORM='Facebook' and SERVICE_TYPE='Story-图片';
update CooperationType set SERVICE_TYPE='定制视频' where COOPERATION_PLATFORM='TikTok' and SERVICE_TYPE='全片';
update CooperationType set SERVICE_TYPE='视频横图' where COOPERATION_PLATFORM='Twitch' and SERVICE_TYPE='Banner Sponsor';


# 增加
insert into CooperationType (COOPERATION_PLATFORM, SERVICE_TYPE, SERVICE_TYPE_EN) values ('Instagram', 'Feed-多图轮播', 'Feed - Multi-picture carousel');
insert into CooperationType (COOPERATION_PLATFORM, SERVICE_TYPE, SERVICE_TYPE_EN) values ('Instagram', 'Story-多图轮播', 'Story - Multi-picture carousel');
insert into CooperationType (COOPERATION_PLATFORM, SERVICE_TYPE, SERVICE_TYPE_EN) values ('Instagram', '首页-简介link', 'Front Page - Introduction Link');
insert into CooperationType (COOPERATION_PLATFORM, SERVICE_TYPE, SERVICE_TYPE_EN) values ('TikTok', '商城链接', 'Online Store link');
insert into CooperationType (COOPERATION_PLATFORM, SERVICE_TYPE, SERVICE_TYPE_EN) values ('TikTok', '转发IG Feed', 'Forward IG Feed');
insert into CooperationType (COOPERATION_PLATFORM, SERVICE_TYPE, SERVICE_TYPE_EN) values ('TikTok', '转发IG Story', 'Forward IG Story');
insert into CooperationType (COOPERATION_PLATFORM, SERVICE_TYPE, SERVICE_TYPE_EN) values ('TikTok', '品牌挑战赛', 'Brand Challenge');
insert into CooperationType (COOPERATION_PLATFORM, SERVICE_TYPE, SERVICE_TYPE_EN) values ('Twitch', '视频插片', 'Integrated video');
 
# 填充服务类型英文名和量词英文名
# youtube
update CooperationType set SERVICE_TYPE_EN='Dedicated Video', QUANTIFIER_EN='min' where PLATFORM='Youtube' and SERVICE_TYPE='全片' and QUANTIFIER='分';
update CooperationType set SERVICE_TYPE_EN='Integrated video', QUANTIFIER_EN='min' where PLATFORM='Youtube' and SERVICE_TYPE='插片' and QUANTIFIER='秒';		# 单位改变 1
update CooperationType set SERVICE_TYPE_EN='Live Streaming', QUANTIFIER_EN='h' where PLATFORM='Youtube' and SERVICE_TYPE='直播' and QUANTIFIER='小时';
update CooperationType set SERVICE_TYPE_EN='Video Authorization' where PLATFORM='Youtube' and SERVICE_TYPE='素材授权' and QUANTIFIER='月'; 						# 单位取消
update CooperationType set SERVICE_TYPE_EN='Community Post' where PLATFORM='Youtube' and SERVICE_TYPE='社区Post' and QUANTIFIER='条'; 							# 单位取消
update CooperationType set SERVICE_TYPE_EN='Link below the video' where PLATFORM='Youtube' and SERVICE_TYPE='视频下方Link';
# instagram
update CooperationType set SERVICE_TYPE_EN='Feed - Single Picture' where PLATFORM='Instagram' and SERVICE_TYPE='Feed-单图' and QUANTIFIER='张'; 				# 单位取消
update CooperationType set SERVICE_TYPE_EN='Feed - Video', QUANTIFIER_EN='min' where PLATFORM='Instagram' and SERVICE_TYPE='Feed-视频' and QUANTIFIER='秒'; 	# 单位改变 2
update CooperationType set SERVICE_TYPE_EN='Story - Single Picture' where PLATFORM='Instagram' and SERVICE_TYPE='Story-单图' and QUANTIFIER='张'; # 单位取消
update CooperationType set SERVICE_TYPE_EN='Story- Video', QUANTIFIER_EN='min' where PLATFORM='Instagram' and SERVICE_TYPE='Story-视频' and QUANTIFIER='秒'; 	# 单位改变 3 
update CooperationType set SERVICE_TYPE_EN='Story - Link' where PLATFORM='Instagram' and SERVICE_TYPE='Story-链接插入';
update CooperationType set SERVICE_TYPE_EN='Authorization', QUANTIFIER_EN='month' where PLATFORM='Instagram' and SERVICE_TYPE='授权' and QUANTIFIER='月'; 		# 仅在原数据库
# facebook
update CooperationType set SERVICE_TYPE_EN='Feed - Single Picture' where PLATFORM='Facebook' and SERVICE_TYPE='Feed-单图' and QUANTIFIER='张'; 					# 单位取消
update CooperationType set SERVICE_TYPE_EN='Feed - Video', QUANTIFIER_EN='min' where PLATFORM='Facebook' and SERVICE_TYPE='Feed-视频' and QUANTIFIER='秒'; 		# 单位改变 4
update CooperationType set SERVICE_TYPE_EN='Story - Single Picture' where PLATFORM='Facebook' and SERVICE_TYPE='Story-单图' and QUANTIFIER='张'; 				# 单位取消  
update CooperationType set SERVICE_TYPE_EN='Story- Video', QUANTIFIER_EN='min' where PLATFORM='Facebook' and SERVICE_TYPE='Story-视频' and QUANTIFIER='秒'; 	# 单位改变 5 
#TikTok
update CooperationType set SERVICE_TYPE_EN='Customized video', QUANTIFIER_EN='min' where PLATFORM='TikTok' and SERVICE_TYPE='定制视频' and QUANTIFIER='秒'; 	# 单位改变 6 
update CooperationType set SERVICE_TYPE_EN='Live Streaming', QUANTIFIER_EN='h' where PLATFORM='TikTok' and SERVICE_TYPE='直播' and QUANTIFIER='分'; 			# 单位改变 7
update CooperationType set SERVICE_TYPE_EN='Brief Introduction Link' where PLATFORM='TikTok' and SERVICE_TYPE='简介link'; 										# 仅在原数据库
#Twitch
update CooperationType set SERVICE_TYPE_EN='Live Streaming', QUANTIFIER_EN='h' where PLATFORM='Twitch' and SERVICE_TYPE='直播' and QUANTIFIER='小时';
update CooperationType set SERVICE_TYPE_EN='Video Banner' where PLATFORM='Twitch' and SERVICE_TYPE='视频横图' and QUANTIFIER='月';								# 单位取消
#其他
update CooperationType set SERVICE_TYPE_EN='Source Material' where PLATFORM='其他' and SERVICE_TYPE='素材' and QUANTIFIER='条';								 	# 仅在原数据库
update CooperationType set SERVICE_TYPE_EN='Dubbing' where PLATFORM='其他' and SERVICE_TYPE='配音' and QUANTIFIER='条';           							# 仅在原数据库

## CooperationType
alter table CooperationType add column SERVICE_TYPE_EN varchar(64) not null default '' comment '服务类型英文名' after SERVICE_TYPE;
alter table CooperationType add column QUANTIFIER_EN varchar(64) not null default '' comment '量词英文名' after QUANTIFIER;
```

```java
default void saveOrUpdateKolPersonalImInfo(Long userId, KolBasicPersonalInfoAndGdprParam.ImInfo imInfo){
   KolPersonalImInfo nowKolPersonalImInfo = this.selectOne(Wrappers.<KolPersonalImInfo>lambdaQuery()
            .eq(KolPersonalImInfo::getUserId, userId)
            .eq(KolPersonalImInfo::getImType, imInfo.getImType()));
   if(null != nowKolPersonalImInfo){
      nowKolPersonalImInfo.setImNum(imInfo.getImNum());
   }else{
      KolPersonalImInfo.KolPersonalImInfoBuilder kolPersonalImInfoBuilder = KolPersonalImInfo.builder()
               .userId(userId)
               .imType(imInfo.getImType())
               .imNum(imInfo.getImNum())
               .createTime(LocalDateTime.now())
               .lastModTime(LocalDateTime.now());
      if( IM_TYPE_OTHERS == imInfo.getImType()){
            kolPersonalImInfoBuilder.imNameOthers(imInfo.getImType().toString());
      }
      this.insert(kolPersonalImInfoBuilder.build());
   }
}
```

```java
//已有KOL标签
List<Long> oldKolTags = new ArrayList<>();
for (KolSelfTag kolSelfTag : kolSelfTagMapper.getAllTagsByUserIdAndSocialAccount(userId, socialMediaAccountId, KOL_PERSONAL_TAG))
   oldKolTags.add(kolSelfTag.getTagId());
//已有内容标签
List<Long> oldContTags = new ArrayList<>();
for (KolSelfTag kolSelfTag : kolSelfTagMapper.getAllTagsByUserIdAndSocialAccount(userId, socialMediaAccountId, KOL_CONTENT_TAG))
   oldContTags.add(kolSelfTag.getTagId());
//最新的KOL标签和内容标签
List<Long> newKolTags = kolSocialMediaAccountParam.getKolTags(), newContTags = kolSocialMediaAccountParam.getContentTags();
//待插入KOL标签
List<Long> toInsertKol = newKolTags.stream().filter(tag -> !oldKolTags.contains(tag)).collect(Collectors.toList());
//待删除KOL标签
List<Long> toDeleteKol = oldKolTags.stream().filter(tag -> !newKolTags.contains(tag)).collect(Collectors.toList());
//待插入内容标签
List<Long> toInsertCont = newContTags.stream().filter(tag -> !oldContTags.contains(tag)).collect(Collectors.toList());
//待删除内容标签
List<Long> toDeleteCont = oldContTags.stream().filter(tag -> ! newContTags.contains(tag)).collect(Collectors.toList());
```

```sql
<insert id="saveOrUpdate">
   insert into KolSelfTag (
      USER_ID,
      SOCIAL_MEDIA_ACCOUNT_ID,
      TAG_TYPE,
      TAG_ID
   )
   values
   <foreach collection="kolTags" item="kolTag" separator=",">
   (
      #{userId},
      #{socialMediaAccountId},
      #{tagType},
      #{kolTag}
   )
   </foreach>
   on duplicate key update
   USER_ID = values(USER_ID),
   tagType = values(tagType)
</insert>
```

```sql
<select id="countByQueryParam" resultType="java.lang.Long">
   select count(1) from OrderBase t1 inner join OrderOnline t2 on t1.ID = t2.ORDER_BASE_ID
   <include refid="taskParam"/>
   where 1 = 1
   <include refid="commonCondition"/>
</select>
```

```sql
<resultMap id="OnlineDetailResultMap" type="outfox.ead.kolplatform.pojo.vo2.OrderOnlineDetailVo">
        <result column="ID" property="orderBaseId"/>
        <result column="ORDER_SN" property="orderSn"/>
        <result column="CUSTOMER_COMPANY_ID" property="customerCompanyId"/>
        <result column="CUSTOMER_COMPANY_NAME" property="customerCompanyName"/>
        <result column="CUSTOMER_INDUSTRY" property="customerIndustry"/>
        <result column="CUSTOMER_LEVEL" property="customerLevel"/>
        <result column="CUSTOMER_PRODUCT" property="customerProduct"/>
        <result column="PRINCIPAL_AM_ID" property="principalAmId"/>
        <result column="PRINCIPAL_AM_NAME" property="principalAmName"/>
        <result column="ORDER_AMOUNT_TOTAL" property="orderAmountTotalCent"/>
        <result column="LAST_ORDER_AMOUNT" property="lastOrderAmountCent"/>
        <result column="STATUS" property="status"/>
        <result column="STAGE" property="stage"/>
        <result column="CREATE_DATE" property="createDate"/>
        <result column="TERMINATION_TYPE" property="terminationType"/>
        <result column="TERMINATION_REASON" property="terminationReason"/>
        <result column="TERMINATION_TIME" property="terminationTime"/>
    </resultMap>
```

```sql
<trim prefix="and ( " suffix=" )">
      t1.ORDER_SN like #{keyword}
      or t1.CUSTOMER_COMPANY_NAME like concat('%', #{keyword}, '%')
      or t1.CUSTOMER_PRODUCT like concat('%', #{keyword}, '%')
</trim>
```

```sql

select * from OrderBase t1 inner join OrderOnline t2 on t1.ID = t2.ORDER_BASE_ID 
inner join (
   select ORDER_BASE_ID, 
         GROUP_CONCAT(ID) AS TASK_ID_STR, 
         GROUP_CONCAT(TASK_STATUS) AS TASK_STATUS_STR,
         GROUP_CONCAT(TASK_STAGE) AS TASK_STAGE_STR,
         GROUP_CONCAT(FINAL_BID) AS FINAL_BID_STR, 
         GROUP_CONCAT(KOL_PLATFORM) AS KOL_PLATFORM_STR,
         GROUP_CONCAT(KOL_NAME) AS KOL_NAME_STR, 
         GROUP_CONCAT(AM_NAME) AS AM_NAME_STR
         from TaskOnline
         where OP_STATUS = 1
         group by ORDER_BASE_ID
) t3 on t1.ID = t3.ORDER_BASE_ID
```

```sql
CREATE TABLE `TaskCooperationType` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CREATOR_ID` bigint(20) NOT NULL COMMENT '创建者ID',
  `ORDER_BASE_ID` bigint(20) NOT NULL COMMENT '订单基础ID',
  `TASK_ID` bigint(20) NOT NULL COMMENT '任务ID',
  `STAGE` tinyint(4) NOT NULL DEFAULT '0' COMMENT '任务阶段：0-未知 2-售前 3-下订 4-上线',
  `COOPERATION_PLATFORM` varchar(64) NOT NULL DEFAULT '' COMMENT '合作平台 YouTube TikTok Instagram Twitch Facebook Twitter',
  `SERVICE_TYPE` varchar(64) NOT NULL DEFAULT '' COMMENT '服务类型',
  `FURTHER_CONDITION` varchar(128) NOT NULL DEFAULT '' COMMENT '细分条件',
  `LAST_MOD_USER_ID` bigint(20) NOT NULL COMMENT '最后修改人',
  `CREATE_TIME` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `LAST_MOD_TIME` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '最后修改时间',
  `OP_STATUS` tinyint(4) NOT NULL DEFAULT '1' COMMENT '逻辑删除: 0-删除 1-有效',
  PRIMARY KEY (`ID`),
  KEY `idx_order` (`ORDER_BASE_ID`),
  KEY `idx_task` (`TASK_ID`,`STAGE`)
) ENGINE=InnoDB AUTO_INCREMENT=566 DEFAULT CHARSET=utf8mb4 COMMENT='任务合作类型表 | dingyh | 2021-06-25'
```

```sql
CREATE TABLE `TaskOnline` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CREATOR_ID` bigint(20) NOT NULL COMMENT '创建者ID',
  `ORDER_BASE_ID` bigint(20) NOT NULL COMMENT '订单基础ID',
  `TASK_STATUS` tinyint(4) NOT NULL DEFAULT '0' COMMENT '任务状态： 0-Approved 1-Pending 2-Cancel',
  `COST` bigint(20) NOT NULL DEFAULT '0' COMMENT '运营报价，单位美分',
  `LAST_COST` bigint(20) NOT NULL DEFAULT '0' COMMENT '最新成本价，单位美分',
  `FINAL_BID` bigint(20) NOT NULL DEFAULT '0' COMMENT '运营实际报价，单位美分',
  `KOL_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT 'KOL ID',
  `KOL_NAME` varchar(128) NOT NULL DEFAULT '' COMMENT 'KOL 昵称',
  `KOL_PLATFORM` varchar(256) NOT NULL DEFAULT '' COMMENT 'KOL 平台',
  `AM_NAME` varchar(128) NOT NULL DEFAULT '' COMMENT '负责运营名字',
  `AM_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '负责运营ID',
  `ESTIMATED_DELIVERY_DATE` date DEFAULT NULL COMMENT '预计交付日期',
  `REMARK` varchar(2048) NOT NULL DEFAULT '' COMMENT '任务备注',
  `TASK_STAGE` tinyint(4) NOT NULL DEFAULT '0' COMMENT '任务阶段： 0-脚本未提交 1-脚本已提交 2-初稿已提交 3-终搞已提交 4-已上线',
  `SCRIPT_MEDIA_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '脚本存储ID',
  `SCRIPT_MEDIA_TIME` timestamp(3) NULL DEFAULT NULL COMMENT '脚本存储时间',
  `FIRST_DRAFT_MEDIA_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '初稿存储ID',
  `FIRST_DRAFT_MEDIA_TIME` timestamp(3) NULL DEFAULT NULL COMMENT '初稿存储时间',
  `FINAL_DRAFT_MEDIA_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '终搞存储ID',
  `FINAL_DRAFT_MEDIA_TIME` timestamp(3) NULL DEFAULT NULL COMMENT '终搞存储时间',
  `ONLINE_URL` varchar(512) NOT NULL DEFAULT '' COMMENT '上线链接',
  `ONLINE_URL_TIME` timestamp(3) NULL DEFAULT NULL COMMENT '上线链接时间',
  `TASK_CONFIRM_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '继承于下订任务ID， 0代表新建',
  `PAYMENT_RATIO` int(11) DEFAULT NULL COMMENT '预付款比例, 单位百分比%',
  `PAYMENT_STAGE` tinyint(4) DEFAULT NULL COMMENT '预付节点：0-订单下订 1-脚本已提交 2-初稿已提交 3-终搞已提交',
  `LAST_MOD_USER_ID` bigint(20) NOT NULL COMMENT '最后修改人',
  `CREATE_TIME` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `LAST_MOD_TIME` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '最后修改时间',
  `OP_STATUS` tinyint(4) NOT NULL DEFAULT '1' COMMENT '逻辑删除: 0-删除 1-有效',
  PRIMARY KEY (`ID`),
  KEY `idx_order` (`ORDER_BASE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=184 DEFAULT CHARSET=utf8mb4 COMMENT='上线任务表 | dingyh | 2021-06-28'
```

```sql
CREATE TABLE `OrderOnline` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CREATOR_ID` bigint(20) NOT NULL COMMENT '创建者ID',
  `ORDER_BASE_ID` bigint(20) NOT NULL COMMENT '订单基础ID',
  `ORDER_AMOUNT_TOTAL` bigint(20) NOT NULL DEFAULT '0' COMMENT '总订单金额，单位美分',
  `LAST_ORDER_AMOUNT` bigint(20) NOT NULL DEFAULT '0' COMMENT '最新订单金额，单位美分',
  `PRINCIPAL_AM_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '主运营ID',
  `PRINCIPAL_AM_NAME` varchar(128) NOT NULL DEFAULT '' COMMENT '主运营名称',
  `SALE_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '负责销售ID',
  `SALE_NAME` varchar(128) NOT NULL DEFAULT '' COMMENT '负责销售名字',
  `STATUS` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态： 0-未提交 1-已提交',
  `LAST_MOD_USER_ID` bigint(20) NOT NULL COMMENT '最后修改人',
  `CREATE_TIME` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `LAST_MOD_TIME` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '最后修改时间',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uk_order` (`ORDER_BASE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=154 DEFAULT CHARSET=utf8mb4 COMMENT='订单上线表 | dingyh | 2021-06-28'
```

```sql
CREATE TABLE `OrderBase` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ORDER_SN` varchar(32) NOT NULL COMMENT '订单号',
  `CUSTOMER_COMPANY_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '客户公司ID',
  `CUSTOMER_COMPANY_NAME` varchar(64) NOT NULL DEFAULT '' COMMENT '客户公司名称',
  `CUSTOMER_INDUSTRY` varchar(64) NOT NULL DEFAULT '' COMMENT '客户公司行业',
  `CUSTOMER_INDUSTRY_TYPE` int(11) NOT NULL DEFAULT '0' COMMENT '行业类型ID',
  `CUSTOMER_LEVEL` varchar(64) NOT NULL DEFAULT '' COMMENT '客户公司级别 S A B C',
  `CUSTOMER_PRODUCT_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '客户产品ID',
  `CUSTOMER_PRODUCT` varchar(64) NOT NULL DEFAULT '' COMMENT '客户投放产品',
  `CONTACT_DETAILS` varchar(256) NOT NULL DEFAULT '' COMMENT '联系方式，JSON 格式',
  `STAGE` tinyint(4) NOT NULL DEFAULT '0' COMMENT '阶段：0-未知 1-触达 2-售前 3-下订 4-上线 9-终止',
  `TERMINATION_TYPE` tinyint(4) DEFAULT NULL COMMENT '终止类型：0-未知 1-输单 2-无效',
  `TERMINATION_REASON` varchar(512) DEFAULT NULL COMMENT '终止原因 json 字符串',
  `TERMINATION_USER_ID` bigint(20) DEFAULT NULL COMMENT '终止操作人',
  `TERMINATION_TIME` timestamp(3) NULL DEFAULT NULL COMMENT '终止时间',
  `CREATE_TIME` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `LAST_MOD_TIME` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '最后修改时间',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uk_order` (`ORDER_SN`)
) ENGINE=InnoDB AUTO_INCREMENT=192 DEFAULT CHARSET=utf8mb4 COMMENT='订单基础表 | dingyh | 2021-06-28'
```

```sql
CREATE TABLE `KolSocialMediaAccount` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户id，对应KolPlatformUser.ID',
  `KOL_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT 'kol id，对应StoredKol.USER_ID',
  `PLATFORM` varchar(255) NOT NULL DEFAULT '' COMMENT '社媒平台类型',
  `SOCIAL_MEDIA_ID` varchar(255) NOT NULL DEFAULT '' COMMENT '外部社交媒体ID，例如youtube的userId',
  `SOCIAL_MEDIA_USERNAME` varchar(255) NOT NULL DEFAULT '' COMMENT '外部社交媒体账号的昵称',
  `HOMEPAGE` varchar(1000) NOT NULL DEFAULT '' COMMENT '社媒平台主页地址',
  `AVATAR_URL` varchar(1000) NOT NULL DEFAULT '' COMMENT '社交媒体头像链接',
  `ACCESS_TOKEN_ENCRYPT` varchar(255) NOT NULL DEFAULT '' COMMENT 'open api返回的access_token，加密存储',
  `ACCESS_TOKEN_CREATE` timestamp(3) NULL DEFAULT NULL COMMENT 'access_token的刷新时间',
  `ACCESS_TOKEN_EXPIRE` timestamp(3) NULL DEFAULT NULL COMMENT 'access_token的过期时间',
  `REFRESH_TOKEN_ENCRYPT` varchar(255) NOT NULL DEFAULT '' COMMENT 'open api返回的refresh_token，加密存储',
  `REFRESH_TOKEN_CREATE` timestamp(3) NULL DEFAULT NULL COMMENT 'refresh_token的刷新时间',
  `REFRESH_TOKEN_EXPIRE` timestamp(3) NULL DEFAULT NULL COMMENT 'refresh_token的过期时间',
  `GRANT_STATUS` tinyint(1) NOT NULL DEFAULT '1' COMMENT '授权状态，1-授权中，2-撤销授权',
  `CREATE_TIME` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `LAST_MOD_TIME` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uk_kol_id` (`USER_ID`,`KOL_ID`),
  KEY `idx_user_id` (`USER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='KOL和社媒平台的绑定关系'
```

```sql
CREATE TABLE `KolSelfTag` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户id，对应KolPlatformUser.ID',
  `SOCIAL_MEDIA_ACCOUNT_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户的社交媒体关系id，对应KolSocialMediaAccount.ID',
  `TAG_TYPE` tinyint(1) NOT NULL DEFAULT '0' COMMENT '标签类型：1-kol标签；2-内容标签；',
  `TAG_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '标签id，对应KolTag.ID',
  `CREATE_TIME` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `LAST_MOD_TIME` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uk_tag_relation` (`SOCIAL_MEDIA_ACCOUNT_ID`,`TAG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='KOL给自己打的标签'
```

```sql
CREATE TABLE `KolSelfQuote` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户id，对应KolPlatformUser.ID',
  `SOCIAL_MEDIA_ACCOUNT_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户的社交媒体关系id，对应KolSocialMediaAccount.ID',
  `COOPERATION_TYPE_UNIT_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '社媒合作类型的报价单位ID，对应CooperationTypeUnit.ID',
  `PRICE` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '社媒合作价格，保留小数点后两位',
  `CURRENCY` tinyint(4) NOT NULL DEFAULT '0' COMMENT '价格币种：1-美元/USD；2-欧元/EUR；3-日元/JPY；4-港币/HKD',
  `CREATE_TIME` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `LAST_MOD_TIME` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uk_id_type` (`SOCIAL_MEDIA_ACCOUNT_ID`,`COOPERATION_TYPE_UNIT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='KOL自己填写的报价信息'
```

```sql
CREATE TABLE `KolPersonalInfo` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` bigint(20) NOT NULL COMMENT '用户id，对应KolPlatformUser.ID',
  `FULL_NAME` varchar(255) DEFAULT NULL COMMENT '用户全名',
  `GENDER_ENCRYPT` varchar(255) DEFAULT NULL COMMENT '性别：0-未知；1-男；2-女；加密存储',
  `BIRTHDATE_ENCRYPT` varchar(255) DEFAULT NULL COMMENT '出生日期，加密存储',
  `COUNTRY_CODE` varchar(20) DEFAULT NULL COMMENT '国家，二位码',
  `PHONE_NUM_ENCRYPT` varchar(255) DEFAULT NULL COMMENT '手机号码，加密存储',
  `COMPLETE_GDPR` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否完成了GDPR问卷：1-已完成；0-未完成；',
  `IS_GDPR_USER` tinyint(1) DEFAULT NULL COMMENT '是否是受GDPR保护的用户：1-是；0-否；',
  `BELONG_TO_EU` tinyint(1) DEFAULT NULL COMMENT '国籍是否为欧盟成员国：1-是；0-否；',
  `CURRENT_IN_EU` tinyint(1) DEFAULT NULL COMMENT '目前所在地是否为欧盟成员国：1-是；0-否；',
  `ALREADY_SIXTEEN` tinyint(1) DEFAULT NULL COMMENT '目前是否已满16岁：1-是；0-否；',
  `ACCEPT_ACCESS` tinyint(1) DEFAULT NULL COMMENT '是否授权读取隐私信息：1-是；0-否；',
  `ACCEPT_RECOMMEND` tinyint(1) DEFAULT NULL COMMENT '是否授权推荐相关订单和广告：1-是；0-否；',
  `CREATE_TIME` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `LAST_MOD_TIME` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `OP_STATUS` tinyint(1) NOT NULL DEFAULT '1' COMMENT '逻辑删除: 0-删除 1-有效',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uk_user_id` (`USER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8mb4 COMMENT='KOL用户的个人信息表'
```

```sql
   CREATE TABLE `CooperationTypeUnit` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `COOPERATION_TYPE_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '社媒合作类型ID，对应CooperationType.ID',
  `SITUATION` tinyint(4) NOT NULL DEFAULT '0' COMMENT '报价单位的应用场景：1-支付，2-kol自己报价',
  `UNIT` varchar(64) NOT NULL DEFAULT '' COMMENT '社媒合作类型报价单位',
  `UNIT_EN` varchar(64) NOT NULL DEFAULT '' COMMENT '社媒合作类型报价单位英文名',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uk_id_type` (`COOPERATION_TYPE_ID`,`SITUATION`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8mb4 COMMENT='社媒合作类型的报价单位表' 
```

```sql
CREATE TABLE `StoredKol` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` bigint(20) NOT NULL COMMENT '储备KOL的用户id',
  `INTERMEDIARY_USER_ID` bigint(20) DEFAULT NULL COMMENT '媒介人的用户id',
  `AVATAR_URL` varchar(500) NOT NULL DEFAULT '' COMMENT '头像地址',
  `NICKNAME` varchar(200) NOT NULL DEFAULT '' COMMENT '昵称',
  `KOL_STATUS` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'KOL状态: 1-未处理 2-不合适 3-储备-有IM 4-储备-无IM',
  `HOME_PAGE_URL` varchar(500) NOT NULL COMMENT '主页',
  `country` varchar(100) NOT NULL DEFAULT '' COMMENT '国家',
  `FAN_NUM` bigint(20) NOT NULL DEFAULT '0' COMMENT '粉丝数',
  `AVG_WATCH_COUNT` bigint(20) NOT NULL DEFAULT '0' COMMENT '平均观看量',
  `DESCRIPTION` text COMMENT '简介',
  `CATEGORY_SORT` varchar(255) NOT NULL DEFAULT '' COMMENT '内容类别',
  `MALE_FAN_RATIO` decimal(5,2) DEFAULT NULL COMMENT '男性粉丝百分比，40.12表示40.12%',
  `MAIL` varchar(255) NOT NULL DEFAULT '' COMMENT '邮箱信息',
  `MAIL_ENCRYPT` varchar(1023) NOT NULL DEFAULT '' COMMENT '邮箱，加密字段',
  `INVITE_AT` date DEFAULT NULL COMMENT '发出邀请日期',
  `OBTAIN_IM_AT` date DEFAULT NULL COMMENT '获取IM号码日期',
  `REMARK` varchar(1000) NOT NULL DEFAULT '' COMMENT '备注',
  `BUSINESS_COOPERATION_OVERVIEW` text COMMENT '商业合作概况',
  `QUOTE` varchar(200) NOT NULL DEFAULT '' COMMENT '报价',
  `KOL_CASE` varchar(1000) NOT NULL DEFAULT '' COMMENT 'CASE',
  `IM_TYPE` tinyint(4) DEFAULT NULL COMMENT 'IM形式: 1-WhatsApp 2-Line 3-Facebook Messenger 4-电话号码 5-Zalo 6-KAKAO TALK 7-VK 8-Skype 9-QQ 10-WeChat 11-Others',
  `IM` varchar(100) NOT NULL DEFAULT '' COMMENT 'IM号码',
  `IM_ENCRYPT` varchar(1023) NOT NULL DEFAULT '' COMMENT 'IM号码，加密字段',
  `ASSIGN_AT` timestamp(3) NULL DEFAULT NULL COMMENT '分配给媒介人的时间',
  `SOURCE` tinyint(4) NOT NULL COMMENT '来源: 1-自拓 2-普通代理 3-爬虫 4-独家代理 5-探索爬虫',
  `INTERMEDIARY_NAME` varchar(100) NOT NULL DEFAULT '' COMMENT '媒介人名字',
  `OP_STATUS` tinyint(4) NOT NULL DEFAULT '1' COMMENT '逻辑删除: 0-删除 1-有效',
  `UPDATE_USER_ID` bigint(20) DEFAULT '0' COMMENT '最新操作的管理员userId',
  `CREATE_USER_ID` bigint(20) DEFAULT '0' COMMENT '创建管理员userId',
  `PLATFORM` varchar(50) NOT NULL DEFAULT '' COMMENT 'KOL 社交媒体平台',
  `AM_USER_ID` bigint(20) DEFAULT NULL COMMENT '跟进运营用户id',
  `AM_NAME` varchar(200) DEFAULT '' COMMENT '跟进运营用户名字',
  `KOL_LEVEL` int(10) DEFAULT '0' COMMENT 'kol评级--0表示尚未评级；1表示A级别；2表示B级别；3表示C级别；4表示S级别',
  `SALE_QUOTE` varchar(255) NOT NULL DEFAULT '' COMMENT '销售报价',
  `LANGUAGE_CODE` varchar(63) DEFAULT '' COMMENT '语言类别代码',
  `SALE_CASE` varchar(1023) DEFAULT '' COMMENT '售前case',
  `LIKES` bigint(20) NOT NULL DEFAULT '0' COMMENT '喜欢/点赞数',
  `WATCH` bigint(20) NOT NULL DEFAULT '0' COMMENT '观看量',
  `MEDIA_COUNT` bigint(20) NOT NULL DEFAULT '0' COMMENT '发布内容数',
  `IF_ASSIGN` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否分配: 0-分配 1-不需要分配 但需要识别语言 2-不分配',
  `QUOTE_DATE` date DEFAULT NULL COMMENT '报价日期',
  `REFUSE_INVITE` tinyint(1) DEFAULT '1' COMMENT '是否拒绝邀请邮件',
  `SEND_INVITE_TIMES` tinyint(4) NOT NULL DEFAULT '0' COMMENT '邀请邮件已发送次数',
  `LAST_INVITE_TIME` timestamp NULL DEFAULT NULL COMMENT '邮件最后一次发送时间',
  `TAG_LOCKED` tinyint(4) NOT NULL DEFAULT '0' COMMENT '标签审核状态， 0-未在审核， 1-待审核确认',
  `AM_ASSIGN_AT` timestamp(3) NULL DEFAULT NULL COMMENT '分配给运营的时间',
  `MCN_QUOTE` varchar(511) DEFAULT NULL COMMENT 'MCN报价',
  `IS_IMPORT_BY_MCN` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否由MCN代理导入，1-是，0-否',
  `FANS_DISTRIBUTION_COUNTRY` varchar(2046) NOT NULL DEFAULT '' COMMENT '粉丝的国家分布，json格式',
  `FANS_DISTRIBUTION_AGE` varchar(511) NOT NULL DEFAULT '' COMMENT '粉丝的年龄分布，json格式',
  `FANS_DISTRIBUTION_GENDER` varchar(255) NOT NULL DEFAULT '' COMMENT '粉丝的性别分布，json格式',
  `NEED_CRAWL` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否由MCN代理导入，1-是，0-否',
  `KOL_TYPE` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'KOL账户类型 0 - 普通账号 1 - 官方账号 2 - 违禁账号',
  `BAN_REASON` varchar(63) NOT NULL DEFAULT '' COMMENT '违禁原因',
  `COOPERATION_TIMES` int(5) NOT NULL DEFAULT '0' COMMENT '合作次数，指该kol和我们平台的合作次数',
  `LAST_30_MEDIA_AVG_WATCH_COUNT` decimal(15,2) DEFAULT NULL COMMENT '该kol近30条媒体的平均观看数',
  `LAST_30_MEDIA_AVG_REMARK_COUNT` decimal(15,2) DEFAULT NULL COMMENT '该kol近30条媒体的平均评论数',
  `LAST_30_MEDIA_AVG_LIKE_COUNT` decimal(15,2) DEFAULT NULL COMMENT '该kol近30条媒体的平均点赞数',
  `LAST_30_MEDIA_AVG_REPOST_COUNT` decimal(15,2) DEFAULT NULL COMMENT '该kol近30条媒体的平均转发数',
  `LAST_30_MEDIA_AVG_INTERACTION_RATE` decimal(15,4) DEFAULT NULL COMMENT '该kol近30条媒体的平均互动率',
  `LAST_MOD_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uk_home_page_url` (`HOME_PAGE_URL`),
  UNIQUE KEY `USER_ID` (`USER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=36228 DEFAULT CHARSET=utf8mb4 COMMENT='储备KOL信息表 | jizhch | 2020-11-12'
```

```java
       List<CooperationType> allTypes = cooperationTypeMapper.getAllCooperationType();
       Map<String, List<PlatformServicesVo.ServiceType>> map = new HashMap<>();
       for (CooperationType type : allTypes) {
           PlatformServicesVo.ServiceType serviceType = new PlatformServicesVo.ServiceType();
           serviceType.setCooperationTypeId(type.getId());
           serviceType.setServiceType(type.getServiceTypeEn());
           //报价单位
           CooperationTypeUnit cooperationTypeUnit = cooperationTypeUnitMapper.getByCooperationTypeIdAndSituation(type.getId(), SELF_QUOTE);
           serviceType.setQuantifier(cooperationTypeUnit.getUnitEn());
           map.putIfAbsent(type.getCooperationPlatform(), new ArrayList<>());
           map.get(type.getCooperationPlatform()).add(serviceType);
       }
       List<PlatformServicesVo> result = new ArrayList<>();
       for (Map.Entry<String, List<PlatformServicesVo.ServiceType>> entry : map.entrySet()) {
           result.add(PlatformServicesVo.builder()
                   .cooperationPlatform(entry.getKey())
                   .serviceTypeList(entry.getValue()).build());
       }
       return result;
```

```java
       List<KolSelfQuote> selfQuotes = kolSelfQuoteMapper.getAllByUserIdAndSocialAccount(userId, kolSocialMediaAccount.getId());
       List<KolSocialMediaAccountInfoVo.CooperationTypeVo> cooperationTypeVos = new ArrayList<>();
       for (KolSelfQuote quote : selfQuotes) {
           CooperationTypeUnit cooperationTypeUnit = cooperationTypeUnitMapper.getById(quote.getCooperationTypeUnitId());
           CooperationType cooperationType = cooperationTypeMapper.getById(cooperationTypeUnit.getCooperationTypeId());
           cooperationTypeVos.add(KolSocialMediaAccountInfoVo.CooperationTypeVo.builder()
                   .cooperationTypeId(cooperationTypeUnit.getCooperationTypeId())
                   .cooperationPlatform(kolSocialMediaAccount.getPlatform())
                   .serviceType(cooperationType.getServiceTypeEn())
                   .quantifier(cooperationTypeUnit.getUnitEn())
                   .currency(quote.getCurrency())
                   .prices(quote.getPrice()).build());
       }
```

```java
Long socialMediaAccountId = kolSocialMediaAccountMapper.getByUserId(userId).getId();
//已有标签
List<Long> oldTags = new ArrayList<>();
for (KolSelfTag kolSelfTag : kolSelfTagMapper.getAllTagsByUserIdAndSocialAccount(userId, socialMediaAccountId)) {
   oldTags.add(kolSelfTag.getTagId());
}
//最新标签
List<Long> newTags = ListUtils.union(kolSocialMediaAccountParam.getKolTags(), kolSocialMediaAccountParam.getContentTags());
//待插入标签
List<Long> toInsert = newTags.stream().filter(tag -> !oldTags.contains(tag)).collect(Collectors.toList());
//待删除标签
List<Long> toDelete = oldTags.stream().filter(tag -> !newTags.contains(tag)).collect(Collectors.toList());
kolSelfTagMapper.insertBatch(userId, socialMediaAccountId, toInsert);
kolSelfTagMapper.deleteBatch(userId, socialMediaAccountId, toDelete);

//已有合作类型(的报价单位表记录)
List<KolSelfQuote> kolSelfQuotes = kolSelfQuoteMapper.getAllByUserIdAndSocialAccount(userId, socialMediaAccountId);
List<Long> oldCooperationTypeUnitIds = kolSelfQuotes.stream().map(kolSelfQuote -> kolSelfQuote.getCooperationTypeUnitId()).collect(Collectors.toList());
//最新合作类型
List<KolSocialMediaAccountParam.CooperationType> newTypes = kolSocialMediaAccountParam.getCooperationTypes();
//CooperationTypeId 和 CooperationTypeUnitId对应关系
Map<Long, Long> typeUnit = newTypes.stream()
         .collect(Collectors.toMap(type -> type.getCooperationTypeId(), type -> cooperationTypeUnitMapper.getByCooperationTypeIdAndSituation(type.getCooperationTypeId(), SELF_QUOTE).getId()));
List<Long> newCooperationTypeUnitIds = (List<Long>) typeUnit.values();
//待插入合作类型
List<Long> toInsertTypes = newCooperationTypeUnitIds.stream().filter(typeId -> !oldCooperationTypeUnitIds.contains(typeId)).collect(Collectors.toList());
//待删除合作类型
List<Long> toDeleteTypes = oldCooperationTypeUnitIds.stream().filter(typeId -> !newCooperationTypeUnitIds.contains(typeId)).collect(Collectors.toList());

for (KolSocialMediaAccountParam.CooperationType cooperationType : newTypes) {
   if (toInsertTypes.contains(typeUnit.get(cooperationType.getCooperationTypeId()))) {
         kolSelfQuoteMapper.insertOne(userId,
               socialMediaAccountId,
               typeUnit.get(cooperationType.getCooperationTypeId()),
               cooperationType.getPrices(),
               cooperationType.getCurrency());
   }
}
kolSelfQuoteMapper.deleteBatch(userId, socialMediaAccountId, toDeleteTypes);
```