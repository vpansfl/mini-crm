package com.minicrm.backend.customer;

import com.minicrm.backend.common.SyncResult;
import com.minicrm.backend.deal.DealResponse;
import com.minicrm.backend.deal.DealService;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	private final CustomerService customerService;
	private final CustomerSyncService customerSyncService;
	private final DealService dealService;

	public CustomerController(CustomerService customerService, CustomerSyncService customerSyncService,
			DealService dealService) {
		this.customerService = customerService;
		this.customerSyncService = customerSyncService;
		this.dealService = dealService;
	}

	@GetMapping
	public List<CustomerResponse> getCustomers() {
		return customerService.getCustomers();
	}

	@GetMapping("/{id}")
	public CustomerResponse getCustomer(@PathVariable @Positive Long id) {
		return customerService.getCustomer(id);
	}

	@GetMapping("/{id}/deals")
	public List<DealResponse> getCustomerDeals(@PathVariable @Positive Long id) {
		return dealService.getDealsByCustomer(id);
	}

	@PostMapping("/sync")
	public SyncResult syncFromHubSpot() {
		return customerSyncService.syncFromHubSpot();
	}

}
