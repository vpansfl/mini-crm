package com.minicrm.backend.hubspot;

import org.springframework.http.HttpStatus;

// HubSpot API 호출 실패. 메시지는 그대로 클라이언트에 응답되므로 비밀값을 포함하지 않는다.
public class HubSpotApiException extends RuntimeException {

	private final HttpStatus status;

	public HubSpotApiException(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}

	public HubSpotApiException(HttpStatus status, String message, Throwable cause) {
		super(message, cause);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

}
