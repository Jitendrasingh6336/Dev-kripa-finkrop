package com.example.dev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.dev.request.LeadRequest;
import com.example.dev.request.UpdateLeadRequest;
import com.example.dev.response.ApiResponse;
import com.example.dev.service.ILeadService;
import com.example.dev.util.Constants;

@RestController
@RequestMapping("/lead")
@CrossOrigin("*")
public class LeadController {
	
	@Autowired
	private ILeadService leadService;
	
	
	@PostMapping("/api/v1/add")
	public ResponseEntity<ApiResponse> addLead(@RequestBody LeadRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(this.leadService.createLead(request));
	}
	
	@GetMapping("/api/v1/get-leads")
    public ResponseEntity<ApiResponse> getLeads(
    		   @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
               @RequestParam(defaultValue = Constants.DEFAULT_PAGE_LIMIT, required = false) Integer pageSize,
               @RequestParam(defaultValue = "", required = false) String search) {
        return ResponseEntity.ok(this.leadService.getAllLeads(pageNumber, pageSize, search));
    }
	
	@GetMapping("/api/v1/get-lead")
	public ResponseEntity<ApiResponse> getLead(@RequestParam("leadId") String id){
		return ResponseEntity.ok(this.leadService.getLeadById(id));
	}
	
	@GetMapping("/api/v1/get-leads-by-status")
    public ResponseEntity<ApiResponse> getLeadsByStatus(
    		   @RequestParam(defaultValue = "0", required = false) String status,
    		   @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
               @RequestParam(defaultValue = Constants.DEFAULT_PAGE_LIMIT, required = false) Integer pageSize,
               @RequestParam(defaultValue = "", required = false) String search) {
        return ResponseEntity.ok(this.leadService.getLeadsByStatus(status, pageNumber, pageSize, search));
    }
	
	// ✅ Update Lead
    @PutMapping("/api/v1/update")
    public ResponseEntity<ApiResponse> updateLead(@RequestBody UpdateLeadRequest request) {
        return ResponseEntity.ok(leadService.updateLead(request));
    }

    // ✅ Update Lead Status
    @PatchMapping("/api/v1/update-status")
    public ResponseEntity<ApiResponse> updateLeadStatus(
            @RequestParam String id,
            @RequestParam String status) {

        return ResponseEntity.ok(leadService.updateLeadStatus(status, id));
    }

    // ✅ Delete Lead (Optional)
    @DeleteMapping("/api/v1/delete")
    public ResponseEntity<ApiResponse> deleteLead(@RequestParam String id) {
        return ResponseEntity.ok(leadService.deleteLead(id));
    }
	
    @GetMapping("/api/v1/count")
    public ResponseEntity<ApiResponse> getLeadCounts() {
        return ResponseEntity.ok(leadService.getLeadCounts());
    }

}
