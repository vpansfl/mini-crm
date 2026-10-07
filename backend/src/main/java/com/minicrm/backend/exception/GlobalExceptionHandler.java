package com.minicrm.backend.exception;

import com.minicrm.backend.hubspot.HubSpotApiException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// 모든 API 오류를 RFC 9457 ProblemDetail 형식으로 응답한다.
// { "type", "title", "status", "detail", "instance" } (+ 검증 오류는 "errors": [{ "field", "message" }])
// 경로 변수 검증 실패(@Positive 등), 타입 오류, 읽을 수 없는 요청 본문 등 Spring MVC 기본 예외는 상위 클래스가 400으로 처리한다.
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ResourceNotFoundException.class)
	public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
		problem.setTitle(ex.getTitle());
		return problem;
	}

	@ExceptionHandler(InvalidRequestException.class)
	public ProblemDetail handleInvalidRequest(InvalidRequestException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
		problem.setTitle("Validation Error");
		return problem;
	}

	@ExceptionHandler(ConflictException.class)
	public ProblemDetail handleConflict(ConflictException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
		problem.setTitle("Conflict");
		return problem;
	}

	@ExceptionHandler(HubSpotApiException.class)
	public ProblemDetail handleHubSpotApi(HubSpotApiException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(ex.getStatus(), ex.getMessage());
		problem.setTitle("HubSpot API Error");
		return problem;
	}

	// @Valid 요청 본문 검증 실패: 필드별 메시지를 함께 응답한다.
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		List<Map<String, String>> errors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(error -> Map.of(
						"field", error.getField(),
						"message", error.getDefaultMessage() != null ? error.getDefaultMessage() : "올바르지 않은 값입니다."))
				.toList();
		String detail = errors.isEmpty() ? "요청 값이 올바르지 않습니다."
				: errors.stream().map(error -> error.get("field") + ": " + error.get("message"))
						.reduce((a, b) -> a + ", " + b)
						.orElseThrow();

		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
		problem.setTitle("Validation Error");
		problem.setProperty("errors", errors);
		return handleExceptionInternal(ex, problem, headers, HttpStatus.BAD_REQUEST, request);
	}

	// 처리되지 않은 예외는 내부 정보를 숨기고 500으로 응답한다.
	@ExceptionHandler(Exception.class)
	public ProblemDetail handleUnexpected(Exception ex) {
		log.error("처리되지 않은 예외", ex);
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
				"서버 내부 오류가 발생했습니다.");
		problem.setTitle("Internal Server Error");
		return problem;
	}

}
