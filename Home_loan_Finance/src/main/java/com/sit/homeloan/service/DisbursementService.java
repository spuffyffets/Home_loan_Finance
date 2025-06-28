package com.sit.homeloan.service;

import com.sit.homeloan.model.Disbursement;

public interface DisbursementService {
    String disburseLoan(Long loanAppId, Double amount);
    Disbursement getByLoanAppId(Long loanAppId);
}
