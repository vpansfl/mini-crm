package com.minicrm.backend.hubspot;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hubspot")
public record HubSpotProperties(
		String baseUrl,
		String accessToken,
		int pageSize,
		Duration connectTimeout,
		Duration readTimeout) {

	public boolean hasAccessToken() {
		return accessToken != null && !accessToken.isBlank();
	}

	// record 기본 toString()은 모든 필드를 출력하므로, 로그 등에 토큰이 노출되지 않도록 재정의한다.
	@Override
	public String toString() {
		return "HubSpotProperties[baseUrl=" + baseUrl
				+ ", accessToken=" + (hasAccessToken() ? "****" : "(not set)")
				+ ", pageSize=" + pageSize
				+ ", connectTimeout=" + connectTimeout
				+ ", readTimeout=" + readTimeout + "]";
	}

}
