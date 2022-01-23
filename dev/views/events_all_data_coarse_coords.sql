CREATE VIEW `events_all_data_coarse_coords` AS
select  
    event.ip as ip,
    event.date as date,
    event.country as country,
    event.country_code as country_code,
    event.city as city,
    round(event.longitude,1) as longitude,
    round(event.latitude,1) as latitude,
    event.requests as requests,
    item.id as item_id,
    item.title as title,
    item.publisher_id as publisher_id,
    item.publisher_name as publisher_name,
    item.authors as authors,
    item.doi as doi,
    item.isbn as isbn,
    item.type as type,
    item.year as year,
    item.grant_number as grant_number,
    item.grant_program as grant_program,
    funder.id as funder_id,
    funder.name as funder_name
from
	event 
		inner join item on event.item_id = item.id
		left join (
			item_funder inner join funder on item_funder.funder_id = funder.id    
		) on item_funder.item_id = item.id 