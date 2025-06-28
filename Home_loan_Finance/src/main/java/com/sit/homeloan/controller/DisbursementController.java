package com.sit.homeloan.controller;

import com.sit.homeloan.model.Disbursement;
import com.sit.homeloan.service.DisbursementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/disbursement")
public class DisbursementController {

    @Autowired
    private DisbursementService disbursementService;

    @PostMapping("/do/{loanAppId}")
    public ResponseEntity<String> disburseLoan(@PathVariable Long loanAppId, @RequestBody Disbursement disbursement) {
        String result = disbursementService.disburseLoan(loanAppId, disbursement.getDisbursedAmount());

        if (result.contains("not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } else if (result.contains("already exists")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
    }

    @GetMapping("/view/{loanAppId}")
    public ResponseEntity<?> getDisbursementByLoanAppId(@PathVariable Long loanAppId) {
        Disbursement disbursement = disbursementService.getByLoanAppId(loanAppId);

        if (disbursement == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No disbursement found for LoanApp ID: " + loanAppId);
        } else {
            return ResponseEntity.ok(disbursement);
        }
    }
}
