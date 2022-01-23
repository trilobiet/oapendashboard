package org.oapen.dashboard.api.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;

import org.oapen.dashboard.api.entities.EventMonthlyCountsPerItemRow;

public class EventMonthlyCountsPerItemRowMapper implements MonthsRowMapper<EventMonthlyCountsPerItemRow> {

	@Override
	public EventMonthlyCountsPerItemRow mapRow(ResultSet rs, int rowNumber) throws SQLException {
		
		EventMonthlyCountsPerItemRow row = new EventMonthlyCountsPerItemRow();
		YearMonth ym = YearMonth.parse(rs.getString("month"));
		
		row.setId(rs.getString("id"));
		row.setIsbn(rs.getString("isbn"));
		row.setTitle(rs.getString("title"));
		row.setPublisherName(rs.getString("publisher_name"));
		row.setDoi(rs.getString("doi"));
		row.setType(rs.getString("type"));
		row.setFunders(rs.getString("funders"));
		row.setYearMonth(ym);
		row.setTotal(rs.getInt("total"));
		row.setMonthTotals(monthTotals(rs,ym));
		
		return row;
	}

}
