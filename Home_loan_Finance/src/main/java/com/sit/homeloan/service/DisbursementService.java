package com.sit.homeloan.service;

import org.springframework.http.ResponseEntity;

import com.sit.homeloan.model.Disbursement;

public interface DisbursementService {
    ResponseEntity<String> disburseLoan(Long loanAppId, Double amount);
    ResponseEntity<?> getByLoanAppId(Long loanAppId);
    Disbursement getDisbursementObjectByLoanAppId(Long loanAppId); // optional internal use
}

