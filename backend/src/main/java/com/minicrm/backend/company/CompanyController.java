package com.minicrm.backend.company;

import com.minicrm.backend.customer.CustomerResponse;
import com.minicrm.backend.customer.CustomerService;
import com.minicrm.backend.deal.DealResponse;
import com.minicrm.backend.deal.DealService;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

	private final CompanyService companyService;
	private final CustomerService customerService;
	private final DealService dealService;

	public CompanyController(CompanyService companyService, CustomerService customerService,
			DealService dealService) {
		this.companyService = companyService;
		this.customerService = customerService;
		this.dealService = dealService;
	}

	@GetMapping
	public List<CompanyResponse> getCompanies() {
		return companyService.getCompanies();
	}

	@GetMapping("/{id}")
	public CompanyResponse getCompany(@PathVariable @Positive Long id) {
		return companyService.getCompany(id);
	}

	@GetMapping("/{id}/customers")
	public List<CustomerResponse> getCompanyCustomers(@PathVariable @Positive Long id) {
		return customerService.getCustomersByCompany(id);
	}

	@GetMapping("/{id}/deals")
	public List<DealResponse> getCompanyDeals(@PathVariable @Positive Long id) {
		return dealService.getDealsByCompany(id);
	}

}
