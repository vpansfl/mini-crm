package com.minicrm.backend.deal;

import com.minicrm.backend.company.Company;
import com.minicrm.backend.customer.Customer;
import com.minicrm.backend.hubspot.HubSpotDeal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "deals")
public class Deal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// HubSpot Deal ID
	@Column(name = "hubspot_id", nullable = false, unique = true, length = 64)
	@Size(max = 64)
	private String hubspotId;

	@Column(name = "deal_name", length = 255)
	@Size(max = 255)
	private String dealName;

	@Column(name = "amount", precision = 19, scale = 2)
	private BigDecimal amount;

	// HubSpot 파이프라인/단계 내부 ID (예: default, appointmentscheduled)
	@Column(name = "pipeline", length = 64)
	@Size(max = 64)
	private String pipeline;

	@Column(name = "deal_stage", length = 64)
	@Size(max = 64)
	private String dealStage;

	@Column(name = "close_date")
	private Instant closeDate;

	// HubSpot Deal의 대표 회사
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private Company company;

	// HubSpot Deal에 연결된 Contact들
	@ManyToMany
	@JoinTable(
			name = "deal_customers",
			joinColumns = @JoinColumn(name = "deal_id"),
			inverseJoinColumns = @JoinColumn(name = "customer_id"))
	private Set<Customer> customers = new HashSet<>();

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

	protected Deal() {
	}

	public Deal(String hubspotId) {
		this.hubspotId = hubspotId;
	}

	// HubSpot 값으로 갱신한다. 값이나 연결된 고객이 실제로 바뀐 경우 true를 반환한다.
	public boolean applyHubSpot(HubSpotDeal source, Company company, Set<Customer> customers) {
		List<Object> before = snapshot();
		this.dealName = source.dealName();
		this.amount = source.amount();
		this.pipeline = source.pipeline();
		this.dealStage = source.dealStage();
		this.closeDate = source.closeDate();
		this.company = company;
		this.hubspotCreatedAt = source.createdAt();
		this.hubspotUpdatedAt = source.updatedAt();
		boolean customersChanged = !this.customers.equals(customers);
		if (customersChanged) {
			// 컬렉션을 교체하지 않고 내용만 바꿔야 Hibernate가 변경분만 반영한다.
			this.customers.retainAll(customers);
			this.customers.addAll(customers);
		}
		return customersChanged || !before.equals(snapshot());
	}

	private List<Object> snapshot() {
		return Arrays.asList(dealName, amount, pipeline, dealStage, closeDate, company, hubspotCreatedAt,
				hubspotUpdatedAt);
	}

	public Long getId() {
		return id;
	}

	public String getHubspotId() {
		return hubspotId;
	}

	public String getDealName() {
		return dealName;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getPipeline() {
		return pipeline;
	}

	public String getDealStage() {
		return dealStage;
	}

	public Instant getCloseDate() {
		return closeDate;
	}

	public Company getCompany() {
		return company;
	}

	public Set<Customer> getCustomers() {
		return customers;
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
