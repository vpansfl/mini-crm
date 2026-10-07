package com.minicrm.backend.customer;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByHubspotId(String hubspotId);

	List<Customer> findAllByHubspotIdIn(Collection<String> hubspotIds);

	List<Customer> findAllByCompany_IdOrderByIdDesc(Long companyId);

}
