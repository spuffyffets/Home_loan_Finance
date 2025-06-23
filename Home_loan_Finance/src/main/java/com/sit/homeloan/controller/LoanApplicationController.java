package com.sit.homeloan.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sit.homeloan.dto.LoanApplicationDTO;
import com.sit.homeloan.service.LoanApplicationService;

@RestController

public class LoanApplicationController {

    @Autowired
    private LoanApplicationService service;

    // ✅ Upload a Single Document with Validation Boolean
    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocument(
            @RequestParam String applicationNumber,
            @RequestParam String documentType,
            @RequestParam MultipartFile file,
            @RequestParam boolean isValid) throws IOException {

        service.uploadDocument(applicationNumber, documentType, file, isValid);
        return ResponseEntity.ok("Document uploaded successfully");
    }

    // ✅ Get Complete Data of Application with All Files in Base64
    @GetMapping("/{applicationNumber}")
    public ResponseEntity<LoanApplicationDTO> getApplication(@PathVariable String applicationNumber) {
        LoanApplicationDTO dto = service.getApplicationByNumber(applicationNumber);
        return ResponseEntity.ok(dto);
    }
}
