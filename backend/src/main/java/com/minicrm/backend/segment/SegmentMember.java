package com.minicrm.backend.segment;

import com.minicrm.backend.customer.Customer;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "segment_members",
		uniqueConstraints = @UniqueConstraint(name = "uk_segment_members_segment_customer",
				columnNames = { "segment_id", "customer_id" }))
public class SegmentMember {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "segment_id", nullable = false)
	private Segment segment;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	protected SegmentMember() {
	}

	public SegmentMember(Segment segment, Customer customer) {
		this.segment = segment;
		this.customer = customer;
	}

	public Long getId() {
		return id;
	}

	public Segment getSegment() {
		return segment;
	}

	public Customer getCustomer() {
		return customer;
	}

}
