package com.minicrm.backend.deal;

import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/deals")
public class DealController {

	private final DealService dealService;

	public DealController(DealService dealService) {
		this.dealService = dealService;
	}

	@GetMapping
	public List<DealResponse> getDeals() {
		return dealService.getDeals();
	}

	@GetMapping("/{id}")
	public DealResponse getDeal(@PathVariable @Positive Long id) {
		return dealService.getDeal(id);
	}

}
