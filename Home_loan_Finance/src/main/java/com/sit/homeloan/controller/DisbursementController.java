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
//
//    @GetMapping("/view/{loanAppId}")
//    public ResponseEntity<?> getDisbursementByLoanAppId(@PathVariable Long loanAppId) {
//        return disbursementService.getByLoanAppId(loanAppId);
//    }

    @PostMapping("/do/{loanAppId}")
    public String disburseLoan(@PathVariable Long loanAppId, @RequestBody Disbursement disbursement) {
        return disbursementService.disburseLoan(loanAppId, disbursement.getDisbursedAmount());
    }

}