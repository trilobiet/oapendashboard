package org.oapen.dashboard.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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
	
}