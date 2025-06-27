package com.sit.homeloan.controller;

import com.sit.homeloan.dto.LoanApplicationDTO;
import com.sit.homeloan.model.LoanApplication;
import com.sit.homeloan.service.LoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController

public class LoanApplicationController {

    @Autowired
    private LoanApplicationService service;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocument(
            @RequestParam("id") Long id,
            @RequestParam("documentType") String documentType,
            @RequestParam("file") MultipartFile file,
            @RequestParam("isValid") boolean isValid) {

        try {
            service.uploadDocument(id, documentType, file, isValid);
            return ResponseEntity.ok("Document uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanApplicationDTO> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getApplicationById(id));
    }
}
