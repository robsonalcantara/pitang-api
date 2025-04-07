package com.pitang.desafiopitangapi.controllers;

import com.pitang.desafiopitangapi.domain.model.ApiError;
import com.pitang.desafiopitangapi.exceptions.BusinessException;
import com.pitang.desafiopitangapi.exceptions.InvalidTokenException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppContollerAdvice {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiError> handleAppGenericException(BusinessException ex) {
		ApiError apiError = ApiError.builder().message(ex.getMessage()).errorCode(ex.getStatus().value()).build();
		return new ResponseEntity<>(apiError, ex.getStatus());
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<ApiError> handleNotFoundException(InvalidTokenException ex) {
		ApiError apiError = ApiError.builder().message(ex.getMessage()).errorCode(ex.getStatus().value()).build();
		return new ResponseEntity<>(apiError, ex.getStatus());
	}

}
