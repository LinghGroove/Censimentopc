package it.contrader.hardwaretracking.controller;

import it.contrader.hardwaretracking.entity.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
	
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<HttpResponse> badCredentialsException(){
		
		return createHttpResponse(HttpStatus.BAD_REQUEST, "Username/Password incorrect");
	}
	
	@ExceptionHandler(LockedException.class)
	public ResponseEntity<HttpResponse> lockedException(){
		return createHttpResponse(HttpStatus.UNAUTHORIZED, "Your account has been locked");	
	}

	private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message){
		
		HttpResponse httpResponse = new HttpResponse(httpStatus.value(), httpStatus, 
				httpStatus.getReasonPhrase().toUpperCase(), message.toUpperCase());
		
		return new ResponseEntity<>(httpResponse, httpStatus);

	}
	
	
	
}
