package org.oapen.dashboard.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DashboardApplicationTests {

	
	@Test 
	public void testYearMonthValidation() {
		
		LookupRepository repo = new LookupRepository();
		
		// date of lastUpdate (normally retrieved from database)
		LocalDate lastUpdate = LocalDate.of(2022, 1, 5);
		
		// before or during lastUpdate                 lastValidMonth:  
		LocalDate now1 = LocalDate.of(2022, 1, 4);  //   2021-11
		LocalDate now2 = LocalDate.of(2022, 1, 5);  //   2021-11
		
		// after lastUpdate                            lastValidMonth:
		LocalDate now3 = LocalDate.of(2022, 1, 6);  //   2021-12
		LocalDate now4 = LocalDate.of(2022, 1, 7);  //   2021-12
		LocalDate now5 = LocalDate.of(2022, 1, 20); //   2021-12
		
		assertEquals(
			YearMonth.of(2021, 11), repo.lastValidMonth(lastUpdate,now1));
		assertEquals(
			YearMonth.of(2021, 11), repo.lastValidMonth(lastUpdate,now2));
		assertEquals(
			YearMonth.of(2021, 12), repo.lastValidMonth(lastUpdate,now3));
		assertEquals(
			YearMonth.of(2021, 12), repo.lastValidMonth(lastUpdate,now4));
		assertEquals(
			YearMonth.of(2021, 12), repo.lastValidMonth(lastUpdate,now5));
		
	}	

}
