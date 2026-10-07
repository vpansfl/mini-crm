package com.minicrm.backend.company;

import com.minicrm.backend.hubspot.HubSpotCompany;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "companies")
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// HubSpot Company ID
	@Column(name = "hubspot_id", nullable = false, unique = true, length = 64)
	@Size(max = 64)
	private String hubspotId;

	@Column(name = "name", length = 255)
	@Size(max = 255)
	private String name;

	@Column(name = "domain", length = 255)
	@Size(max = 255)
	private String domain;

	@Column(name = "industry", length = 100)
	@Size(max = 100)
	private String industry;

	@Column(name = "phone", length = 64)
	@Size(max = 64)
	private String phone;

	@Column(name = "city", length = 100)
	@Size(max = 100)
	private String city;

	@Column(name = "state", length = 100)
	@Size(max = 100)
	private String state;

	@Column(name = "country", length = 100)
	@Size(max = 100)
	private String country;

	// HubSpot에서의 생성/수정 시각 (createdate, hs_lastmodifieddate)
	@Column(name = "hubspot_created_at")
	private Instant hubspotCreatedAt;

	@Column(name = "hubspot_updated_at")
	private Instant hubspotUpdatedAt;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	protected Company() {
	}

	public Company(String hubspotId) {
		this.hubspotId = hubspotId;
	}

	// HubSpot 값으로 갱신한다. 값이 실제로 바뀐 경우 true를 반환한다.
	public boolean applyHubSpot(HubSpotCompany source) {
		List<Object> before = snapshot();
		this.name = source.name();
		this.domain = source.domain();
		this.industry = source.industry();
		this.phone = source.phone();
		this.city = source.city();
		this.state = source.state();
		this.country = source.country();
		this.hubspotCreatedAt = source.createdAt();
		this.hubspotUpdatedAt = source.updatedAt();
		return !before.equals(snapshot());
	}

	private List<Object> snapshot() {
		return Arrays.asList(name, domain, industry, phone, city, state, country, hubspotCreatedAt, hubspotUpdatedAt);
	}

	public Long getId() {
		return id;
	}

	public String getHubspotId() {
		return hubspotId;
	}

	public String getName() {
		return name;
	}

	public String getDomain() {
		return domain;
	}

	public String getIndustry() {
		return industry;
	}

	public String getPhone() {
		return phone;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getCountry() {
		return country;
	}

	public Instant getHubspotCreatedAt() {
		return hubspotCreatedAt;
	}

	public Instant getHubspotUpdatedAt() {
		return hubspotUpdatedAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

}
