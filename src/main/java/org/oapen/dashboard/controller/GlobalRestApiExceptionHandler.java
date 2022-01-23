package org.oapen.dashboard.controller;

import javax.validation.ConstraintViolationException;

import org.oapen.dashboard.api.entities.RequestError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalRestApiExceptionHandler {
	
	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND )
	public @ResponseBody RequestError handleNotFoundError(NoHandlerFoundException ex) {
	    return new RequestError(404, "resource not found.");
	}
	
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST )
    public RequestError handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        return new RequestError(400, "parameter missing: '" + name);
    }

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST )
	public RequestError handleConstraintViolationError(ConstraintViolationException ex) {
	    return new RequestError(400, "constraint violation: " + ex.getMessage() );
	}
	
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public RequestError handleMalformedParams(MethodArgumentTypeMismatchException ex) {
        String name = ex.getName();
        return new RequestError(422, "parameter is malformed: '" + name);
    }
	
}