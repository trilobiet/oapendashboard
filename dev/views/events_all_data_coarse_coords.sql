CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `trilobiet`@`localhost` 
    SQL SECURITY DEFINER
VIEW `events_all_data_coarse_coords` AS
    SELECT 
        `event`.`ip` AS `ip`,
        `event`.`date` AS `date`,
        `event`.`country` AS `country`,
        `event`.`country_code` AS `country_code`,
        `event`.`city` AS `city`,
        ROUND(`event`.`longitude`, 1) AS `longitude`,
        ROUND(`event`.`latitude`, 1) AS `latitude`,
        `event`.`requests` AS `requests`,
        `event`.`inetAton` AS `inetAton`,
        `item`.`id` AS `item_id`,
        `item`.`title` AS `title`,
        `item`.`publisher_id` AS `publisher_id`,
        `item`.`publisher_name` AS `publisher_name`,
        `item`.`authors` AS `authors`,
        `item`.`doi` AS `doi`,
        `item`.`isbn` AS `isbn`,
        `item`.`type` AS `type`,
        `item`.`year` AS `year`,
        `item`.`grant_number` AS `grant_number`,
        `item`.`grant_program` AS `grant_program`,
        `funder`.`id` AS `funder_id`,
        `funder`.`name` AS `funder_name`
    FROM
        ((`event`
        JOIN `item` ON ((`event`.`item_id` = `item`.`id`)))
        LEFT JOIN (`item_funder`
        JOIN `funder` ON ((`item_funder`.`funder_id` = `funder`.`id`))) ON ((`item_funder`.`item_id` = `item`.`id`)))