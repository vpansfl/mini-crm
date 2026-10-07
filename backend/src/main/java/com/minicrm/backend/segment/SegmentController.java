package com.minicrm.backend.segment;

import com.minicrm.backend.customer.CustomerResponse;
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
@RequestMapping("/api/segments")
public class SegmentController {

	private final SegmentService segmentService;

	public SegmentController(SegmentService segmentService) {
		this.segmentService = segmentService;
	}

	@GetMapping
	public List<SegmentResponse> getSegments() {
		return segmentService.getSegments();
	}

	@GetMapping("/{id}")
	public SegmentResponse getSegment(@PathVariable @Positive Long id) {
		return segmentService.getSegment(id);
	}

	@GetMapping("/{id}/customers")
	public List<CustomerResponse> getSegmentCustomers(@PathVariable @Positive Long id) {
		return segmentService.getSegmentCustomers(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public SegmentResponse createSegment(@RequestBody @Valid SegmentRequest request) {
		return segmentService.createSegment(request);
	}

	@PostMapping("/preview")
	public SegmentPreviewResponse preview(@RequestBody @Valid SegmentPreviewRequest request) {
		return segmentService.preview(request.toRule());
	}

	@PutMapping("/{id}")
	public SegmentResponse updateSegment(@PathVariable @Positive Long id, @RequestBody @Valid SegmentRequest request) {
		return segmentService.updateSegment(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteSegment(@PathVariable @Positive Long id) {
		segmentService.deleteSegment(id);
	}

	@PostMapping("/{id}/refresh")
	public SegmentResponse refreshSegment(@PathVariable @Positive Long id) {
		return segmentService.refreshSegment(id);
	}

}
