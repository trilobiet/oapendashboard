package org.oapen.dashboard.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalRestApiExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RequestError handleMissingParams(AccessDeniedException ex, HttpServletResponse response) {

    	response.setStatus(403);
        return new RequestError(403, "forbidden: " + ex.getMessage());
    }
	
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RequestError handleMissingParams(MissingServletRequestParameterException ex, HttpServletResponse response) {
        
    	response.setStatus(400);
        return new RequestError(400, "parameter missing: '" + ex.getParameterName());
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public RequestError handleConstraintViolationError(ConstraintViolationException ex, HttpServletResponse response) {

		response.setStatus(400);
	    return new RequestError(400, "constraint violation: " + ex.getMessage() );
	}
	
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public RequestError handleMalformedParams(MethodArgumentTypeMismatchException ex, HttpServletResponse response) {
    	
    	response.setStatus(422);
        return new RequestError(422, "parameter is malformed: '" + ex.getName());
    }
	

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<Object> handleSQLError(SQLException ex, WebRequest request) {
		
		HttpHeaders hdrs = new HttpHeaders();
		hdrs.add("Ooooooops", "You can't do that");

		return new ResponseEntity<Object>(
			"SQL Errortje", hdrs, HttpStatus.CONFLICT
		);
	}


}