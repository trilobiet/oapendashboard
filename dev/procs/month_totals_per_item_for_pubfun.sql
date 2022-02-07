CREATE DEFINER=`trilobiet`@`localhost` PROCEDURE `month_totals_per_item_for_pubfun`(
	in fromDate DATE,				# query for this month (and all 11 months before) 
    in publisherIds VARCHAR(1024),	# one or more comma separated publisher id's
    in funderIds VARCHAR(1024),		# one or more comma separated funder id's
    in countryCode CHAR(2),			# query only for a specific country
    in itemType VARCHAR(15),		# query only for a specific type
    in itemId VARCHAR(36)			# query only for a specific title
)
BEGIN
	# Only year and month are available, so dates are stored as the first day of a month.
	# fromMonth converts any date to the first day of the supplied month. 
    declare fromMonth DATE;
    declare yearBefore DATE;
    
    set fromDate = ifnull(fromDate, curdate());
	set fromMonth = date_sub(fromDate, INTERVAL dayofmonth(fromDate)-1 DAY);
    set yearBefore = date_sub(fromMonth, INTERVAL 12 MONTH);
    
	select 
		  id, isbn, title, publisher_name, doi, type
        , group_concat(distinct funders) as funders 
        #, nullif(countryCode,(select country from event where country_code=countryCode limit 1)) as country
		, date_format(fromMonth,"%Y-%m") as month
		, sum( case when date <= date_sub(fromMonth, INTERVAL 0 MONTH) and date >= date_sub(fromMonth, INTERVAL 11 MONTH) then mtot else null end ) total
		, sum( case when date=date_sub(fromMonth, INTERVAL 0 MONTH) then mtot else 0 end ) month_0
		, sum( case when date=date_sub(fromMonth, INTERVAL 1 MONTH) then mtot else 0 end ) month_1
		, sum( case when date=date_sub(fromMonth, INTERVAL 2 MONTH) then mtot else 0 end ) month_2
		, sum( case when date=date_sub(fromMonth, INTERVAL 3 MONTH) then mtot else 0 end ) month_3
		, sum( case when date=date_sub(fromMonth, INTERVAL 4 MONTH) then mtot else 0 end ) month_4
		, sum( case when date=date_sub(fromMonth, INTERVAL 5 MONTH) then mtot else 0 end ) month_5
		, sum( case when date=date_sub(fromMonth, INTERVAL 6 MONTH) then mtot else 0 end ) month_6
		, sum( case when date=date_sub(fromMonth, INTERVAL 7 MONTH) then mtot else 0 end ) month_7
		, sum( case when date=date_sub(fromMonth, INTERVAL 8 MONTH) then mtot else 0 end ) month_8
		, sum( case when date=date_sub(fromMonth, INTERVAL 9 MONTH) then mtot else 0 end ) month_9
		, sum( case when date=date_sub(fromMonth, INTERVAL 10 MONTH) then mtot else 0 end ) month_10
		, sum( case when date=date_sub(fromMonth, INTERVAL 11 MONTH) then mtot else 0 end ) month_11
	from (
		select 
			  date, item.id as id, isbn, title, publisher_name, doi, type
            , group_concat(distinct funder.name) as funders  
            , sum(requests) as mtot
		from item inner join event on item.id = event.item_id
		left join (
			item_funder inner join funder on item_funder.funder_id = funder.id    
		) on item_funder.item_id = item.id 
		where 
			date > yearBefore 
			and if( itemId is null, true, item.id = itemId )
			and if( publisherIds is null, true, FIND_IN_SET(publisher_id, publisherIds) )
            and if( funderIds is null, true, FIND_IN_SET(funder_id, funderIds) )
            and if( itemType is null, true, item.type = itemType )
            and if( countryCode is null, true, event.country_code = countryCode )
		group by date, item.id
	) tmp
	group by id
    having total is not null
	order by total desc
    ;
    
END