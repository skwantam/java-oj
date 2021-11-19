[TOC]
## Dev Tools

### git
http://www.ruanyifeng.com/blog/2015/12/git-workflow.html (GitHub流程)

http://www.ruanyifeng.com/blog/2015/08/git-use-process.html

https://blog.carbonfive.com/always-squash-and-rebase-your-git-commits/

https://www.yiibai.com/git/git_pull.html

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

### lombok
https://projectlombok.org/api/

### Serialization
https://juejin.cn/post/7003220566698098695

### Maven
https://blog.csdn.net/zp357252539/article/details/80392101(mvn archetype:generate -DgroupId=com.tsj.oj -DartifactId=javaOj -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false)

### jUnit
https://www.jianshu.com/p/e72c5595710a


## System Logic

### 账号绑定
http://blog.itpub.net/31557372/viewspace-2671003/


## Experience For Dev

https://blog.csdn.net/sinat_15155817/article/details/115214679


## System Design

### api design
https://zhuanlan.zhihu.com/p/56955812


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

### foreign key constraint
constraint fk_CooperationTypeUnit_CooperationType_On_ID
        foreign key (COOPERATION_TYPE_ID) 
		references CooperationType (ID)


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
insert into CooperationType (COOPERATION_PLATFORM, SERVICE_TYPE, SERVICE_TYPE_EN, QUANTIFIER,  QUANTIFIER_EN) values ('Instagram', 'Feed-多图轮播', 'Feed - Multi-picture carousel', default, default);
insert into CooperationType (COOPERATION_PLATFORM, SERVICE_TYPE, SERVICE_TYPE_EN, QUANTIFIER,  QUANTIFIER_EN) values ('Instagram', 'Story-多图轮播', 'Story - Multi-picture carousel', default, default);
insert into CooperationType (COOPERATION_PLATFORM, SERVICE_TYPE, SERVICE_TYPE_EN, QUANTIFIER,  QUANTIFIER_EN) values ('Instagram', '首页-简介link', 'Front Page - Introduction Link', default, default);
insert into CooperationType (COOPERATION_PLATFORM, SERVICE_TYPE, SERVICE_TYPE_EN, QUANTIFIER,  QUANTIFIER_EN) values ('TikTok', '商城链接', 'Online Store link', default, default);
insert into CooperationType (COOPERATION_PLATFORM, SERVICE_TYPE, SERVICE_TYPE_EN, QUANTIFIER,  QUANTIFIER_EN) values ('TikTok', '转发IG Feed', 'Forward IG Feed', default, default);
insert into CooperationType (COOPERATION_PLATFORM, SERVICE_TYPE, SERVICE_TYPE_EN, QUANTIFIER,  QUANTIFIER_EN) values ('TikTok', '转发IG Story', 'Forward IG Story', default, default);
insert into CooperationType (COOPERATION_PLATFORM, SERVICE_TYPE, SERVICE_TYPE_EN, QUANTIFIER,  QUANTIFIER_EN) values ('TikTok', '品牌挑战赛', 'Brand Challenge', default, default);
insert into CooperationType (COOPERATION_PLATFORM, SERVICE_TYPE, SERVICE_TYPE_EN, QUANTIFIER,  QUANTIFIER_EN) values ('Twitch', '视频插片', 'Integrated video', '分', 'min');
 
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

