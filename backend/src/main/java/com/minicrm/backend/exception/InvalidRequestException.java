package com.minicrm.backend.exception;

// 요청 값의 조합이 비즈니스 규칙에 맞지 않을 때 사용한다. GlobalExceptionHandler가 400으로 응답한다.
public class InvalidRequestException extends RuntimeException {

	public InvalidRequestException(String message) {
		super(message);
	}

}
