use kol_overseas;
select 
    t1.Id as `kolId`,
	t1.NICKNAME as `Channel name`, 
    t2.KOl_TAG_STR as `Kol Tags`,
    t3.CONT_TAG_STR as `Content Tags`,
    t1.HOME_PAGE_URL as `Channel link`,
    t1.country as `National`, 
    t1.FAN_NUM as `Subscriber`
from StoredKol t1 inner join 
(	
	select b1.USER_ID, group_concat(b2.NAME separator "/") KOl_TAG_STR
    from KolKolTag b1 inner join KolTag b2 on b1.TAG_ID = b2.ID
    where convert(b1.TAG_ID, char) like "1%" and b2.NAME not like '未知' and  b2.NAME not like '是'and  b2.NAME not like '否'
    group by b1.USER_ID
) t2 on t1.USER_ID = t2.USER_ID
inner join 
(
	select b1.USER_ID, group_concat(b2.NAME separator "/") CONT_TAG_STR
    from KolKolTag b1 inner join KolTag b2 on b1.TAG_ID = b2.ID
    where convert(b1.TAG_ID, char) like "2%" and b2.NAME not like '未知' and  b2.NAME not like '是'and  b2.NAME not like '否'
    group by b1.USER_ID
) t3 on t1.USER_ID = t3.USER_ID
where KOL_TYPE in (0, 1) and OP_STATUS = 1
