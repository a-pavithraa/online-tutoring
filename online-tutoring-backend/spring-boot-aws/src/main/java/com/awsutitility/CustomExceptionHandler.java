package com.awsutitility;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomExceptionHandler  extends ResponseEntityExceptionHandler {

	@ExceptionHandler(IllegalAccessException.class)
	  public ResponseEntity<Object>  handleNotFoundException(Exception ex, org.springframework.http.HttpHeaders headers, HttpStatus status, WebRequest request) {
	
	        return handleExceptionInternal(ex, ex.getMessage(), headers, status, request);
	  }
}
