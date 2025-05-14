package com.BoardGameFinder.BoardGameFinder.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.BoardGameFinder.BoardGameFinder.Exception.BadRequestException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	public ResponseEntity<String> handleBadRequest(BadRequestException ex){
		return ResponseEntity.badRequest().body(ex.getMessage());
	}
}