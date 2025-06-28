package com.sit.homeloan.serviceimpl;

import com.sit.homeloan.model.*;
import com.sit.homeloan.repository.*;
import com.sit.homeloan.service.DisbursementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DisbursementServiceImpl implements DisbursementService {

    @Autowired
    private DisbursementRepository disbursementRepository;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Override
    public String disburseLoan(Long loanAppId, Double amount) {
        LoanApplication loanApp = loanApplicationRepository.findById(loanAppId).orElse(null);

        if (loanApp == null) {
            return "Loan Application not found with ID: " + loanAppId;
        }

        if (disbursementRepository.findByLoanApplication(loanApp).isPresent()) {
            return "Disbursement already exists for this Loan Application.";
        }

        Disbursement disbursement = new Disbursement();
        disbursement.setLoanApplication(loanApp);
        disbursement.setDisbursedAmount(amount);
        disbursement.setDisbursementDate(LocalDate.now());
        disbursement.setDisbursementStatus("DISBURSED");

        disbursementRepository.save(disbursement);

        return "Loan Disbursed Successfully for LoanApp ID: " + loanAppId;
    }

    @Override
    public Disbursement getByLoanAppId(Long loanAppId) {
        LoanApplication loanApp = loanApplicationRepository.findById(loanAppId).orElse(null);
        if (loanApp == null) {
            return null;
        }
        return disbursementRepository.findByLoanApplication(loanApp).orElse(null);
    }
}
