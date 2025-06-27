package com.sit.homeloan.controller;

import com.sit.homeloan.model.Document;
import com.sit.homeloan.service.CreditManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/credit-manager/documents")
@CrossOrigin("*")
public class CreditManagerController {

    @Autowired
    private CreditManagerService creditManagerService;

    @GetMapping("/by-customer-id")
    public List<Document> getDocumentsByCustomerId(@RequestParam Long customerId) {
        return creditManagerService.getDocumentsByCustomerId(customerId);
    }

    @PutMapping("/verify/{id}")
    public String updateDocumentStatus(@PathVariable Long id, @RequestParam String status) {
        creditManagerService.updateVerificationStatus(id, status);
        return "Document status updated to: " + status.toUpperCase();
    }
}
