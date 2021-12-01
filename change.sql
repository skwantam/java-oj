use kol_overseas;

-- CooperationType 新增新的推广合作类型：带中文单位
insert into CooperationType (COOPERATION_PLATFORM, SERVICE_TYPE, SERVICE_TYPE_EN) values ('Instagram', 'Feed-多图轮播', 'Feed - Multi-picture carousel');
insert into CooperationType (COOPERATION_PLATFORM, SERVICE_TYPE, SERVICE_TYPE_EN) values ('Instagram', 'Story-多图轮播', 'Story - Multi-picture carousel');
insert into CooperationType (COOPERATION_PLATFORM, SERVICE_TYPE, SERVICE_TYPE_EN) values ('Instagram', '首页-简介link', 'Front Page - Introduction Link');
insert into CooperationType (COOPERATION_PLATFORM, SERVICE_TYPE, SERVICE_TYPE_EN, QUANTIFIER) values ('TikTok', '商城链接', 'Online Store link', '条');
insert into CooperationType (COOPERATION_PLATFORM, SERVICE_TYPE, SERVICE_TYPE_EN) values ('TikTok', '转发IG Feed', 'Forward IG Feed');
insert into CooperationType (COOPERATION_PLATFORM, SERVICE_TYPE, SERVICE_TYPE_EN) values ('TikTok', '转发IG Story', 'Forward IG Story');
insert into CooperationType (COOPERATION_PLATFORM, SERVICE_TYPE, SERVICE_TYPE_EN) values ('TikTok', '品牌挑战赛', 'Brand Challenge');
insert into CooperationType (COOPERATION_PLATFORM, SERVICE_TYPE, SERVICE_TYPE_EN, QUANTIFIER) values ('Twitch', '视频插片', 'Integrated video', '分');

-- 填充CooperatonTypeUnit：存储原CooperationType表中用于下订单的单位
delimiter $$
drop procedure if exists fillCooperatonTypeUnit$$
create procedure fillCooperatonTypeUnit()
begin
	declare done tinyint default false;
	declare cid int default 0;
    declare unit varchar(64) default '';
	declare cs cursor for (select id from CooperationType);
	declare continue handler for sqlstate '02000' set done=true;
	open cs;
	insert_loop:loop
		fetch cs into cid;
		if done then
      		leave insert_loop;
    	end if;
        select QUANTIFIER into unit from CooperationType where ID = cid;
        if unit = '月' then
			insert into CooperationTypeUnit (COOPERATION_TYPE_ID, SITUATION, UNIT, UNIT_EN) values (cid, 1, unit, 'mon');
		end if;
        if unit = '小时' then
			insert into CooperationTypeUnit (COOPERATION_TYPE_ID, SITUATION, UNIT, UNIT_EN) values (cid, 1, unit, 'h');
		end if;
        if unit = '分' then
			insert into CooperationTypeUnit (COOPERATION_TYPE_ID, SITUATION, UNIT, UNIT_EN) values (cid, 1, unit, 'min');
		end if;
        if unit = '秒' then
			insert into CooperationTypeUnit (COOPERATION_TYPE_ID, SITUATION, UNIT, UNIT_EN) values (cid, 1, unit, 'sec');
		end if;
        if unit = '张' then
			insert into CooperationTypeUnit (COOPERATION_TYPE_ID, SITUATION, UNIT, UNIT_EN) values (cid, 1, unit, 'Per Picture');
		end if;
        if unit = '支' then
			insert into CooperationTypeUnit (COOPERATION_TYPE_ID, SITUATION, UNIT, UNIT_EN) values (cid, 1, unit, 'Per Video');
		end if;
        if unit = '条' then
			insert into CooperationTypeUnit (COOPERATION_TYPE_ID, SITUATION, UNIT, UNIT_EN) values (cid, 1, unit, 'Per');
		end if;
        if unit = '-' then 
			insert into CooperationTypeUnit (COOPERATION_TYPE_ID, SITUATION, UNIT, UNIT_EN) values (cid, 1, unit, default);
		end if;
	end loop;
	close cs;
end$$
delimiter ;
call fillCooperatonTypeUnit();

-- 填充TaskCooperationType的COOPERATION_TYPE_UNIT_ID字段
update TaskCooperationType as t1 inner join CooperationType as t2 on t1.COOPERATION_PLATFORM = t2.COOPERATION_PLATFORM and t1.SERVICE_TYPE = t2.SERVICE_TYPE
inner join CooperationTypeUnit as t3 on t2.ID = t3.COOPERATION_TYPE_ID
set t1.COOPERATION_TYPE_UNIT_ID = t3.ID 
where t1.COOPERATION_PLATFORM is not null and t1.SERVICE_TYPE is not null and t3.SITUATION = 1;


-- 更新CooperationType表中合作类型的名称：名称更新，含义不变
update CooperationType set SERVICE_TYPE='素材授权' where COOPERATION_PLATFORM='Youtube' and SERVICE_TYPE='授权';
update CooperationType set SERVICE_TYPE='Feed-单图' where COOPERATION_PLATFORM='Instagram' and SERVICE_TYPE='Feed-图片';
update CooperationType set SERVICE_TYPE='Story-单图' where COOPERATION_PLATFORM='Instagram' and SERVICE_TYPE='Story-图片';
update CooperationType set SERVICE_TYPE='Feed-单图' where COOPERATION_PLATFORM='Facebook' and SERVICE_TYPE='Feed-图片';
update CooperationType set SERVICE_TYPE='Story-单图' where COOPERATION_PLATFORM='Facebook' and SERVICE_TYPE='Story-图片';
update CooperationType set SERVICE_TYPE='定制视频' where COOPERATION_PLATFORM='TikTok' and SERVICE_TYPE='全片';
update CooperationType set SERVICE_TYPE='视频横图' where COOPERATION_PLATFORM='Twitch' and SERVICE_TYPE='Banner Sponsor';


-- 在CooperationTypeUnit表中新增用于KOL自己报价的单位
insert into CooperationTypeUnit (COOPERATION_TYPE_ID, SITUATION, UNIT, UNIT_EN) values (
	(select ID from CooperationType where COOPERATION_PLATFORM ='Youtube'  and  SERVICE_TYPE = '插片'),
	2,
	'分',
	'min'
);
insert into CooperationTypeUnit (COOPERATION_TYPE_ID, SITUATION, UNIT, UNIT_EN) values (
	(select ID from CooperationType where COOPERATION_PLATFORM = 'Instagram' and  SERVICE_TYPE = 'Feed-视频'),
	2,
	'分',
	'min'
);
insert into CooperationTypeUnit (COOPERATION_TYPE_ID, SITUATION, UNIT, UNIT_EN) values (
	(select ID from CooperationType where COOPERATION_PLATFORM = 'Instagram' and  SERVICE_TYPE = 'Story-视频'),
	2,
	'分',
	'min'
);
insert into CooperationTypeUnit (COOPERATION_TYPE_ID, SITUATION, UNIT, UNIT_EN) values (
	(select ID from CooperationType where COOPERATION_PLATFORM =  'TikTok' and  SERVICE_TYPE = '定制视频'),
	2,
	'分',
	'min'
);
insert into CooperationTypeUnit (COOPERATION_TYPE_ID, SITUATION, UNIT, UNIT_EN) values (
	(select ID from CooperationType where COOPERATION_PLATFORM = 'TikTok' and  SERVICE_TYPE = '直播'),
	2,
	'小时',
	'h'
);
insert into CooperationTypeUnit (COOPERATION_TYPE_ID, SITUATION, UNIT, UNIT_EN) values (
	(select ID from CooperationType where COOPERATION_PLATFORM =  'Facebook' and  SERVICE_TYPE = 'Feed-视频'),
	2,
	'分',
	'min'
);
insert into CooperationTypeUnit (COOPERATION_TYPE_ID, SITUATION, UNIT, UNIT_EN) values (
	(select ID from CooperationType where COOPERATION_PLATFORM = 'Facebook' and  SERVICE_TYPE = 'Story-视频'),
	2,
	'分',
	'min'
);


-- 填充 CooperationTypeUnit 中用于KOL自己报价的单位
delimiter $$
drop procedure if exists addSelfQuoteUnit$$
create procedure addSelfQuoteUnit()
begin
    declare done tinyint default false;
    declare cid bigint(20);
    declare ctid bigint(20);
    declare unitEn varchar(64) character set utf8mb4 default '';
    declare cs cursor for (select ID from CooperationTypeUnit where COOPERATION_TYPE_ID not in (178,190,194,204,206,198,202));
    declare continue handler for sqlstate '02000' set done=true;
    open cs;
    insert_loop:loop
        fetch cs into cid;
        if done then
            leave insert_loop;
        end if;
        select COOPERATION_TYPE_ID into ctid from CooperationTypeUnit where ID = cid;
        select UNIT_EN into unitEn from CooperationTypeUnit where ID = cid;
        insert into CooperationTypeUnit (COOPERATION_TYPE_ID, SITUATION, UNIT, UNIT_EN) values (ctid, 2, unit, unitEn);
   end loop;
   close cs;
end$$
delimiter ;
call addSelfQuoteUnit();

-- 在CooperationType表中填充服务类型英文名
--Youtube
update CooperationType set SERVICE_TYPE_EN='Dedicated Video' where COOPERATION_PLATFORM='Youtube' and SERVICE_TYPE='全片';
update CooperationType set SERVICE_TYPE_EN='Integrated video' where COOPERATION_PLATFORM='Youtube' and SERVICE_TYPE='插片';
update CooperationType set SERVICE_TYPE_EN='Live Streaming' where COOPERATION_PLATFORM='Youtube' and SERVICE_TYPE='直播' ;
update CooperationType set SERVICE_TYPE_EN='Video Authorization' where COOPERATION_PLATFORM='Youtube' and SERVICE_TYPE='素材授权' ;
update CooperationType set SERVICE_TYPE_EN='Community Post' where COOPERATION_PLATFORM='Youtube' and SERVICE_TYPE='社区Post' ;
update CooperationType set SERVICE_TYPE_EN='Link below the video' where COOPERATION_PLATFORM='Youtube' and SERVICE_TYPE='视频下方Link';
-- instagram
update CooperationType set SERVICE_TYPE_EN='Feed - Single Picture' where COOPERATION_PLATFORM='Instagram' and SERVICE_TYPE='Feed-单图' ;
update CooperationType set SERVICE_TYPE_EN='Feed - Video' where COOPERATION_PLATFORM='Instagram' and SERVICE_TYPE='Feed-视频' ;
update CooperationType set SERVICE_TYPE_EN='Story - Single Picture' where COOPERATION_PLATFORM='Instagram' and SERVICE_TYPE='Story-单图' ;
update CooperationType set SERVICE_TYPE_EN='Story- Video' where COOPERATION_PLATFORM='Instagram' and SERVICE_TYPE='Story-视频' ;
update CooperationType set SERVICE_TYPE_EN='Story - Link' where COOPERATION_PLATFORM='Instagram' and SERVICE_TYPE='Story-链接插入';
update CooperationType set SERVICE_TYPE_EN='Authorization' where COOPERATION_PLATFORM='Instagram' and SERVICE_TYPE='授权' ;
-- facebook
update CooperationType set SERVICE_TYPE_EN='Feed - Single Picture' where COOPERATION_PLATFORM='Facebook' and SERVICE_TYPE='Feed-单图' ;
update CooperationType set SERVICE_TYPE_EN='Feed - Video' where COOPERATION_PLATFORM='Facebook' and SERVICE_TYPE='Feed-视频' ;
update CooperationType set SERVICE_TYPE_EN='Story - Single Picture' where COOPERATION_PLATFORM='Facebook' and SERVICE_TYPE='Story-单图' ;
update CooperationType set SERVICE_TYPE_EN='Story- Video' where COOPERATION_PLATFORM='Facebook' and SERVICE_TYPE='Story-视频' ;
-- TikTok
update CooperationType set SERVICE_TYPE_EN='Customized video' where COOPERATION_PLATFORM='TikTok' and SERVICE_TYPE='定制视频' ;
update CooperationType set SERVICE_TYPE_EN='Live Streaming' where COOPERATION_PLATFORM='TikTok' and SERVICE_TYPE='直播' ;
update CooperationType set SERVICE_TYPE_EN='Brief Introduction Link' where COOPERATION_PLATFORM='TikTok' and SERVICE_TYPE='简介link';
-- Twitch
update CooperationType set SERVICE_TYPE_EN='Live Streaming' where COOPERATION_PLATFORM='Twitch' and SERVICE_TYPE='直播' ;
update CooperationType set SERVICE_TYPE_EN='Video Banner' where COOPERATION_PLATFORM='Twitch' and SERVICE_TYPE='视频横图' ;
-- 其他 
update CooperationType set SERVICE_TYPE_EN='Source Material' where COOPERATION_PLATFORM='其他' and SERVICE_TYPE='素材' ;
update CooperationType set SERVICE_TYPE_EN='Dubbing' where COOPERATION_PLATFORM='其他' and SERVICE_TYPE='配音' ;

