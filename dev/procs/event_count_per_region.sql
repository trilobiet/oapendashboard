CREATE DEFINER=`trilobiet`@`localhost` PROCEDURE `event_count_per_region`(

    in startDate DATE,	
    in endDate DATE,

    in lat DOUBLE,					# query in an area defined by a geo location ... 
    in lon DOUBLE,					# ... 
    in radius int,					# ... and radius (in km)
    in countryCode VARCHAR(20),		# query only for specific countries

    in publisherIds VARCHAR(1024),	# one or more comma separated publisher id's
    in funderIds VARCHAR(1024),		# one or more comma separated funder id's
    in itemType VARCHAR(15),		# query only for a specific type (book/chapter)
	in itemId VARCHAR(36)			# query only for a specific title
)
BEGIN
	# Only year and month are available, so dates are stored as the first day of a month.
	# fromMonth converts any date to the first day of the supplied month. 
    declare endMonth, startMonth DATE;
    declare mradius, crudeSquare int;
    
    set endDate = ifnull(endDate, curdate());
	set endMonth = date_sub(endDate, INTERVAL dayofmonth(endDate)-1 DAY);
    set startMonth = date_sub(startDate, INTERVAL dayofmonth(startDate)-1 DAY);
    
    set mradius = radius * 1000; # radius in meter
    # rough square area to limit exact searches 
    # (chosen large enough to account for longitude scaling towards the equator) 
    set crudeSquare = radius / 25; 

	select  
		city, country_code, latitude, longitude, requests
    from (
		select 
			round(avg(latitude),5) as latitude, 
			round(avg(longitude),5) as longitude, 
            any_value(country_code) as country_code,
            city,
            sum(requests) as requests
		from item join event on item.id = event.item_id
		left join (
			item_funder inner join funder on item_funder.funder_id = funder.id    
		) on item_funder.item_id = item.id 
		where 
			date >= startMonth and date <= endMonth
            and if( countryCode is null, true, FIND_IN_SET(event.country_code, countryCode) )
            and abs(latitude-lat) < crudeSquare # These 2 clauses prevent too far out of range locations 
			and	abs(longitude-lon) < crudeSquare # from being sent to (expensive) ST_Distance_Sphere
			and	ST_Distance_Sphere( point(longitude,latitude), point(lon,lat) ) < mradius
			and if( itemId is null, true, item.id = itemId )
            and if( itemType is null, true, item.type = itemType )
			and if( publisherIds is null, true, FIND_IN_SET(publisher_id, publisherIds) )
            and if( funderIds is null, true, FIND_IN_SET(funder_id, funderIds) )
            
		group by city
	) tmp
    order by city
	;
END