package org.oapen.dashboard.api.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;

import org.oapen.dashboard.api.entities.EventMonthlyCountsPerCountryRow;

public class EventMonthlyCountsPerCountryRowMapper implements MonthsRowMapper<EventMonthlyCountsPerCountryRow> {

	@Override
	public EventMonthlyCountsPerCountryRow mapRow(ResultSet rs, int rowNumber) throws SQLException {
		
		EventMonthlyCountsPerCountryRow row = new EventMonthlyCountsPerCountryRow();
		YearMonth ym = YearMonth.parse(rs.getString("month"));
		
		row.setCountry(rs.getString("country"));
		row.setCountryCode(rs.getString("country_code"));
		row.setYearMonth(YearMonth.parse(rs.getString("month")));
		row.setTotal(rs.getInt("total"));
		row.setMonthTotals(monthTotals(rs,ym));
		
		return row;
	}

}
