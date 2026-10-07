package com.minicrm.backend.exception;

// 현재 데이터 상태 때문에 요청을 처리할 수 없을 때 사용한다. GlobalExceptionHandler가 409로 응답한다.
public class ConflictException extends RuntimeException {

	public ConflictException(String message) {
		super(message);
	}

}
