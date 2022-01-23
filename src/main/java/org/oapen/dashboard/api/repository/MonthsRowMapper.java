package org.oapen.dashboard.api.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.jdbc.core.RowMapper;

public interface MonthsRowMapper<T> extends RowMapper<T> {
	
	/**
	 * Aggregate all 'month_x' column values in a map counting the months 
	 * down from the provided date (first is 0).
	 * 
	 * @param rs ResultSet containg columns
	 * @param lastMonth Month to start counting down.
	 * @return A Map mapping year-months to an Int value 
	 */
	default Map<YearMonth,Integer> monthTotals(ResultSet rs, YearMonth lastMonth) {
		
		Map<YearMonth,Integer> map = new TreeMap<>();
		
		int i = 0;
		boolean doContinue = true;
		
		while (doContinue && i<120) { // not more than 10 years back
			try {
				YearMonth ym = lastMonth.minusMonths(i);
				map.put(ym, rs.getInt("month_"+i));
				i++;
			} catch (SQLException e) {
				doContinue = false;
			}
		}
		
		return map;
	}

}
