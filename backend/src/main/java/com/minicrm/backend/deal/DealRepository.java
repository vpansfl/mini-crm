package com.minicrm.backend.deal;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

// 응답에 회사/고객 정보가 포함되므로 EntityGraph로 함께 조회해 N+1 쿼리를 줄인다.
public interface DealRepository extends JpaRepository<Deal, Long> {

	@EntityGraph(attributePaths = { "company", "customers" })
	List<Deal> findAllByOrderByIdDesc();

	@EntityGraph(attributePaths = { "company", "customers" })
	Optional<Deal> findWithDetailsById(Long id);

	@EntityGraph(attributePaths = { "company", "customers" })
	List<Deal> findAllByCompany_IdOrderByIdDesc(Long companyId);

	// customers 조건 조인과 fetch 조인이 섞이지 않도록 customers는 지연 로딩한다.
	@EntityGraph(attributePaths = { "company" })
	List<Deal> findAllByCustomers_IdOrderByIdDesc(Long customerId);

	@EntityGraph(attributePaths = { "customers" })
	List<Deal> findAllByHubspotIdIn(Collection<String> hubspotIds);

}
