package com.sit.homeloan.controller;

import com.sit.homeloan.model.Document;
import com.sit.homeloan.model.SanctionLetter;
import com.sit.homeloan.service.CreditManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/credit-manager")
@CrossOrigin("*")
public class CreditManagerController {

    @Autowired
    private CreditManagerService creditManagerService;

    
    @GetMapping("/documents/by-customer-id")
    public List<Document> getDocumentsByCustomerId(@RequestParam Long customerId) {
        return creditManagerService.getDocumentsByCustomerId(customerId);
    }

    
    @PutMapping("/documents/verify/{id}")
    public String updateDocumentStatus(@PathVariable Long id, @RequestParam String status) {
        creditManagerService.updateVerificationStatus(id, status);
        return "Document status updated to: " + status.toUpperCase();
    }

    
    @PutMapping("/evaluate/{loanAppId}")
    public String evaluateLoan(@PathVariable Long loanAppId) {
        creditManagerService.evaluateLoanApplication(loanAppId);
        return "Loan application evaluated successfully!";
    }

    
    @PostMapping("/sanction/{loanAppId}")
    public SanctionLetter generateSanction(@PathVariable Long loanAppId,
                                           @RequestBody SanctionLetter request) {
        return creditManagerService.generateSanctionLetter(loanAppId, request);
    }

    
    @GetMapping("/sanction/{loanAppId}")
    public SanctionLetter getSanction(@PathVariable Long loanAppId) {
        return creditManagerService.getSanctionLetter(loanAppId);
    }
}
