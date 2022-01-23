CREATE DEFINER=`trilobiet`@`localhost` PROCEDURE `event_count_per_location`(

    in startDate DATE,	
    in endDate DATE,
	in decimals INTEGER,			# number of decimals in lat/lon

    in publisherIds VARCHAR(1024),	# one or more comma separated publisher id's
    in funderIds VARCHAR(1024),		# one or more comma separated funder id's
	in itemId VARCHAR(36),			# query only for a specific title
    in itemType VARCHAR(15),		# query only for a specific type (book/chapter)

	in countryCode CHAR(2),			# query only for a specific country
    in lat DOUBLE,					# query in an area defined by a geo location ... 
    in lon DOUBLE,					# ... 
    in radius int					# ... and radius (in km)
)
BEGIN
	# Only year and month are available, so dates are stored as the first day of a month.
	# fromMonth converts any date to the first day of the supplied month. 
    declare endMonth, startMonth DATE;
    set endDate = ifnull(endDate, curdate());
	set endMonth = date_sub(endDate, INTERVAL dayofmonth(endDate)-1 DAY);
    set startMonth = date_sub(startDate, INTERVAL dayofmonth(startDate)-1 DAY);
    set decimals = ifnull(decimals, 1);

	select  
		city, country_code, latitude, longitude, requests
    from (
		select round(latitude,decimals) as latitude, 
			round(longitude,decimals) as longitude, 
            group_concat(distinct country_code) as country_code, 
            city,
            sum(requests) as requests
		from item join event on item.id = event.item_id
		left join (
			item_funder inner join funder on item_funder.funder_id = funder.id    
		) on item_funder.item_id = item.id 
		where date >= startMonth and date <= endMonth
			and if( itemId is null, true, item.id = itemId )
            and if( itemType is null, true, item.type = itemType )
			and if( publisherIds is null, true, FIND_IN_SET(publisher_id, publisherIds) )
            and if( funderIds is null, true, FIND_IN_SET(funder_id, funderIds) )
            and if( countryCode is null, true, event.country_code = countryCode )
            and if( lon is null or lat is null or radius is null, true,  
				ST_Distance_Sphere( point(longitude,latitude), point(lon,lat) ) / 1000 < radius
            )
		group by city, 
        round(latitude,decimals), round(longitude,decimals)
	) tmp
    order by city
	;
END