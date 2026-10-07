package com.minicrm.backend.customer;

import com.minicrm.backend.company.Company;
import com.minicrm.backend.hubspot.HubSpotContact;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "customers")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// HubSpot Contact ID. HubSpot 연동 전에 생성된 고객은 값이 없을 수 있다.
	@Column(name = "hubspot_id", unique = true, length = 64)
	@Size(max = 64)
	private String hubspotId;

	@Column(name = "first_name", length = 100)
	@Size(max = 100)
	private String firstName;

	@Column(name = "last_name", length = 100)
	@Size(max = 100)
	private String lastName;

	@Column(name = "email", length = 255)
	@Size(max = 255)
	private String email;

	@Column(name = "phone", length = 64)
	@Size(max = 64)
	private String phone;

	@Column(name = "mobile_phone", length = 64)
	@Size(max = 64)
	private String mobilePhone;

	// HubSpot Contact의 company 텍스트 속성. 회사 레코드와의 연결은 company 필드를 사용한다.
	@Column(name = "company_name", length = 255)
	@Size(max = 255)
	private String companyName;

	@Column(name = "job_title", length = 255)
	@Size(max = 255)
	private String jobTitle;

	@Column(name = "city", length = 100)
	@Size(max = 100)
	private String city;

	@Column(name = "state", length = 100)
	@Size(max = 100)
	private String state;

	@Column(name = "country", length = 100)
	@Size(max = 100)
	private String country;

	// HubSpot lifecyclestage (예: lead, customer)
	@Column(name = "lifecycle_stage", length = 64)
	@Size(max = 64)
	private String lifecycleStage;

	// HubSpot hs_lead_status (예: NEW, OPEN)
	@Column(name = "lead_status", length = 64)
	@Size(max = 64)
	private String leadStatus;

	// HubSpot Contact의 대표 회사 (associatedcompanyid)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private Company company;

	// HubSpot에서의 생성/수정 시각 (createdate, lastmodifieddate)
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

	protected Customer() {
	}

	public Customer(String hubspotId) {
		this.hubspotId = hubspotId;
	}

	// HubSpot 값으로 갱신한다. 값이 실제로 바뀐 경우 true를 반환한다.
	public boolean applyHubSpot(HubSpotContact source, Company company) {
		List<Object> before = snapshot();
		this.firstName = source.firstName();
		this.lastName = source.lastName();
		this.email = source.email();
		this.phone = source.phone();
		this.mobilePhone = source.mobilePhone();
		this.companyName = source.company();
		this.jobTitle = source.jobTitle();
		this.city = source.city();
		this.state = source.state();
		this.country = source.country();
		this.lifecycleStage = source.lifecycleStage();
		this.leadStatus = source.leadStatus();
		this.company = company;
		this.hubspotCreatedAt = source.createdAt();
		this.hubspotUpdatedAt = source.updatedAt();
		return !before.equals(snapshot());
	}

	private List<Object> snapshot() {
		return Arrays.asList(firstName, lastName, email, phone, mobilePhone, companyName, jobTitle, city, state,
				country, lifecycleStage, leadStatus, company, hubspotCreatedAt, hubspotUpdatedAt);
	}

	public Long getId() {
		return id;
	}

	public String getHubspotId() {
		return hubspotId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getJobTitle() {
		return jobTitle;
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

	public String getLifecycleStage() {
		return lifecycleStage;
	}

	public String getLeadStatus() {
		return leadStatus;
	}

	public Company getCompany() {
		return company;
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
