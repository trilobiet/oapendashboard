CREATE DEFINER=`trilobiet`@`localhost` PROCEDURE `month_totals_per_item_for_library`(
    
    in fromDate DATE,				# query for this month (and all 11 months before) 
	in libraryId VARCHAR(36),		# library id (from user table)
    
    in publisherIds VARCHAR(1024),	# one or more comma separated publisher id's
    in funderIds VARCHAR(1024),		# one or more comma separated funder id's
    
    in itemType VARCHAR(15),		# query only for a specific type (book/chapter)
    in itemIds VARCHAR(1024)		# query only for specific titles (comma sep) since we limit to 1000 rows
)
BEGIN
	# Only year and month are available, so dates are stored as the first day of a month.
	# fromMonth converts any date to the first day of the supplied month. 
    declare fromMonth DATE;
    declare yearBefore DATE;
    declare countryCode CHAR(2); # TODO maybe allow for comma list then match with FIND_IN_SET
    
    set countryCode = 'XX'; # get library's country code from user table (this narrows the rows to be searched)
    set countryCode = (select country_code from user where id = libraryId limit 1); 

    set fromDate = ifnull(fromDate, curdate());
	set fromMonth = date_sub(fromDate, INTERVAL dayofmonth(fromDate)-1 DAY);
    set yearBefore = date_sub(fromMonth, INTERVAL 12 MONTH);

	select 
		  id, isbn, title, publisher_name, doi, type, 
          group_concat(distinct funders) as funders,
          date_format(fromMonth,"%Y-%m") as month
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
			item.id as id,
            isbn, title, publisher_name, doi, type,
            group_concat(distinct funder.name) as funders,  
			date, sum(requests) as mtot 
			FROM event ev 
				join ip_range l on 
					ip_aton >= ip_start_aton AND ip_aton <= l.ip_end_aton
                join item on item_id = item.id 
                left join (
					item_funder inner join funder on item_funder.funder_id = funder.id    
				) on item_funder.item_id = item.id 
			where country_code = countryCode  
            and user_id = libraryId
            and date > yearBefore
			and date <= fromMonth

			and if( itemIds is null, true, FIND_IN_SET(item.id, itemIds ) )
			and if( publisherIds is null, true, FIND_IN_SET(publisher_id, publisherIds) )
			and if( funderIds is null, true, FIND_IN_SET(funder_id, funderIds) )
            and if( itemType is null, true, item.type = itemType )
        
			group by date, ev.item_id

	) q
	group by id
	having total is not null
	order by total desc
    limit 2500;

END