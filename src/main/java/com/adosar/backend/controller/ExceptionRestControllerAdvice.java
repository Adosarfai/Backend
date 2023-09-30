package com.adosar.backend.controller;

import com.adosar.backend.domain.ExceptionResponseMessage;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ExceptionRestControllerAdvice {

	@ExceptionHandler({ConstraintViolationException.class})
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ExceptionResponseMessage handleInvalidParameterException(RuntimeException ex) {

		return sendResponse(HttpStatus.BAD_REQUEST, ex);
	}

	private ExceptionResponseMessage sendResponse(HttpStatus status, RuntimeException ex) {
		return new ExceptionResponseMessage(Instant.now(), status.value(), status.getReasonPhrase(), ex.getClass().toString(), ex.getMessage());
	}
}

