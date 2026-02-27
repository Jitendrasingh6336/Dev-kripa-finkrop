package com.example.dev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.dev.request.FaqRequest;
import com.example.dev.request.UpdateFaqRequest;
import com.example.dev.response.ApiResponse;
import com.example.dev.service.IFaqService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/faqs")
@CrossOrigin(origins = "*")
@Validated
public class FaqController {

    @Autowired
    private IFaqService faqService;

    /**
     * ➕ Add FAQ
     */
    @PostMapping("/api/v1/web/add")
    public ResponseEntity<ApiResponse> addFaq(@Valid @RequestBody FaqRequest faqRequest) {

        ApiResponse response = faqService.addFaq(faqRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * 📄 Get All FAQs (Pagination + Search)
     */
    @GetMapping("/api/v1/web/get-all-faq")
    public ResponseEntity<ApiResponse> getAllFaqs(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String search) {

        ApiResponse response =
                faqService.getAllFaq(pageNumber, pageSize, search);

        return ResponseEntity.ok(response);
    }

    /**
     * ✏️ Update FAQ
     */
    @PutMapping("/api/v1/web/update-faq")
    public ResponseEntity<ApiResponse> updateFaq(@Valid @RequestBody UpdateFaqRequest updateFaqRequest) {

        ApiResponse response = faqService.updateFaq(updateFaqRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * 🗑️ Delete FAQ (Soft Delete)
     */
    @DeleteMapping("/api/v1/web/delete-faq")
    public ResponseEntity<ApiResponse> deleteFaq(@Valid @NotBlank(message = "id is required")
            @RequestParam String faqId) {

        ApiResponse response = faqService.deleteFaq(faqId);
        return ResponseEntity.ok(response);
    }
}
