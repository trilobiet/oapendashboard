package org.oapen.dashboard.api.repository;

import java.util.List;

import org.oapen.dashboard.api.entities.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class ItemRepository {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;	
	
	// Do not cache
	public List<Item> findByTitle(String title) {
		
		List<Item> lst = jdbcTemplate.query(
			"select * from item where title like ?", 
				new BeanPropertyRowMapper<>(Item.class),
				new Object[] { "%"+title+"%" }
			);
		
		return lst;
	}
	
	// Do not cache
	public List<Item> findByTitleForPublishers(String title, String publishers) {
		
		List<Item> lst = jdbcTemplate.query(
			"select * from item where title like ? and FIND_IN_SET(publisher_id, ?)", 
				new BeanPropertyRowMapper<>(Item.class),
				new Object[] { "%"+title+"%", publishers }
			);
		
		return lst;
	}
	
	// Do not cache	
	public List<Item> findByTitleForFunders(String title, String funders) {
		
		List<Item> lst = jdbcTemplate.query(
			"select * from item join "
			+ "item_funder on item_funder.item_id = item.id "
			+ "where title like ? and FIND_IN_SET(funder_id, ?)", 
				new BeanPropertyRowMapper<>(Item.class),
				new Object[] { "%"+title+"%", funders }
			);
		
		return lst;
	}
	
	
}
