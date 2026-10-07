package com.minicrm.backend.exception;

// 조회 대상이 없을 때 사용하는 공통 예외. GlobalExceptionHandler가 404로 응답한다.
public abstract class ResourceNotFoundException extends RuntimeException {

	private final String title;

	protected ResourceNotFoundException(String title, String message) {
		super(message);
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

}
