package com.minicrm.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 로컬 개발은 Vite 프록시(/api → 8080)를 사용하므로 CORS가 필요 없다.
// 배포 환경(Vercel → Render)은 CORS_ALLOWED_ORIGINS 환경변수(쉼표 구분)의 Origin만 허용한다.
// 패턴 매칭을 사용하므로 Vercel 미리보기 주소(예: https://mini-crm-*.vercel.app)도 지정할 수 있다.
@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final String[] allowedOrigins;

	public WebConfig(@Value("${app.cors.allowed-origins}") String[] allowedOrigins) {
		this.allowedOrigins = allowedOrigins;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**")
				.allowedOriginPatterns(allowedOrigins)
				.allowedMethods("GET", "POST", "PUT", "DELETE");
	}

}
