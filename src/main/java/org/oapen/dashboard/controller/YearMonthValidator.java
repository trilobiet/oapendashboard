package org.oapen.dashboard.controller;

import java.time.YearMonth;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.oapen.dashboard.api.repository.LookupRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class YearMonthValidator implements ConstraintValidator<ValidYearMonth, YearMonth> {
	
	@Autowired
    private LookupRepository lookupRepository;
	
    @Override
    public void initialize(ValidYearMonth constraintAnnotation) {}
	
	@Override
	public boolean isValid(YearMonth ym, ConstraintValidatorContext context) {
		
		boolean isValid = true;
		
		if (ym.isAfter(lookupRepository.lastRequestableMonth()) ) {
			
			isValid = false;
			context.buildConstraintViolationWithTemplate(
				"data not available for period " + ym + " or later").addConstraintViolation();
		}
		else if (ym.isBefore(lookupRepository.firstAvailableMonth()) ) {
			
			isValid = false;
			context.buildConstraintViolationWithTemplate(
				"data not available for period " + ym + " or earlier").addConstraintViolation();
		}
		
		return isValid;
	}

}
