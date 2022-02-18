CREATE DEFINER=`trilobiet`@`localhost` PROCEDURE `event_count_per_country`(

    in startDate DATE,	
    in endDate DATE,

    in publisherIds VARCHAR(1024),	# one or more comma separated publisher id's
    in funderIds VARCHAR(1024),		# one or more comma separated funder id's
    in itemType VARCHAR(15),		# query only for a specific type (book/chapter)
	in itemId VARCHAR(36)			# query only for a specific title
)
BEGIN
	# Only year and month are available, so dates are stored as the first day of a month.
	# fromMonth converts any date to the first day of the supplied month. 
    declare endMonth, startMonth DATE;
    
    set endDate = ifnull(endDate, curdate());
	set endMonth = date_sub(endDate, INTERVAL dayofmonth(endDate)-1 DAY);
    set startMonth = date_sub(startDate, INTERVAL dayofmonth(startDate)-1 DAY);

	select  
		country_code, latitude, longitude, requests
    from (
		select e.country_code, c.latitude, c.longitude, sum(requests) as requests
		from item join event e on item.id = e.item_id
        join countries c on c.country_code = e.country_code
		left join (
			item_funder inner join funder on item_funder.funder_id = funder.id    
		) on item_funder.item_id = item.id 
		where date >= startMonth and date <= endMonth
			and if( itemId is null, true, item.id = itemId )
            and if( itemType is null, true, item.type = itemType )
			and if( publisherIds is null, true, FIND_IN_SET(publisher_id, publisherIds) )
            and if( funderIds is null, true, FIND_IN_SET(funder_id, funderIds) )
		group by e.country_code, c.latitude, c.longitude
	) tmp
    order by country_code;
END