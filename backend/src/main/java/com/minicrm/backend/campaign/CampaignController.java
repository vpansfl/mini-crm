package com.minicrm.backend.campaign;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

	private final CampaignService campaignService;

	public CampaignController(CampaignService campaignService) {
		this.campaignService = campaignService;
	}

	@GetMapping
	public List<CampaignResponse> getCampaigns() {
		return campaignService.getCampaigns();
	}

	@GetMapping("/{id}")
	public CampaignResponse getCampaign(@PathVariable @Positive Long id) {
		return campaignService.getCampaign(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CampaignResponse createCampaign(@RequestBody @Valid CampaignRequest request) {
		return campaignService.createCampaign(request);
	}

	@PutMapping("/{id}")
	public CampaignResponse updateCampaign(@PathVariable @Positive Long id,
			@RequestBody @Valid CampaignRequest request) {
		return campaignService.updateCampaign(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCampaign(@PathVariable @Positive Long id) {
		campaignService.deleteCampaign(id);
	}

	@GetMapping("/{id}/metrics")
	public CampaignMetricsResponse getMetrics(@PathVariable @Positive Long id) {
		return campaignService.getMetrics(id);
	}

	@PostMapping("/{id}/simulate-results")
	public CampaignMetricsResponse simulateResults(@PathVariable @Positive Long id) {
		return campaignService.simulateResults(id);
	}

}
