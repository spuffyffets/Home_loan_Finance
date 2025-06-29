package com.sit.homeloan.serviceimpl;

import com.sit.homeloan.enums.ApplicationStatus;
import com.sit.homeloan.model.*;
import com.sit.homeloan.repository.*;
import com.sit.homeloan.service.DisbursementService;
import com.sit.homeloan.service.LoanStageHistoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class DisbursementServiceImpl implements DisbursementService {

    @Autowired
    private DisbursementRepository disbursementRepository;
    
    @Autowired
    private LoanStageHistoryService loanStageHistoryService;


    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Override
    public ResponseEntity<String> disburseLoan(Long loanAppId, Double amount) {
        Optional<LoanApplication> loanAppOpt = loanApplicationRepository.findById(loanAppId);
        if (loanAppOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Loan Application not found with ID: " + loanAppId);
        }

        LoanApplication loanApp = loanAppOpt.get();

        if (disbursementRepository.findByLoanApplication(loanApp).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Disbursement already exists for this Loan Application.");
        }

        
        Disbursement disbursement = new Disbursement();
        disbursement.setLoanApplication(loanApp);
        disbursement.setDisbursedAmount(amount);
        disbursement.setDisbursementDate(LocalDate.now());
        disbursement.setDisbursementStatus("DISBURSED");
        disbursementRepository.save(disbursement);

        
        loanApp.setApplicationStatus(ApplicationStatus.DISBURSED);
        loanApplicationRepository.save(loanApp);

        
        loanStageHistoryService.logStage(
            loanApp.getId(),
            "Disbursement Manager",
            "DISBURSEMENT_MANAGER",
            ApplicationStatus.DISBURSED.name(),
            "Loan disbursed successfully."
        );

       
        return ResponseEntity.ok("Loan Disbursed Successfully for LoanApp ID: " + loanAppId);
    }


    @Override
    public ResponseEntity<?> getByLoanAppId(Long loanAppId) {
        Optional<LoanApplication> loanAppOpt = loanApplicationRepository.findById(loanAppId);
        if (loanAppOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Loan Application not found");
        }

        Optional<Disbursement> disbOpt = disbursementRepository.findByLoanApplication(loanAppOpt.get());
        if (disbOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No disbursement found for LoanApp ID: " + loanAppId);
        }

        return ResponseEntity.ok(disbOpt.get());
    }

    @Override
    public Disbursement getDisbursementObjectByLoanAppId(Long loanAppId) {
        Optional<LoanApplication> loanAppOpt = loanApplicationRepository.findById(loanAppId);
        if (loanAppOpt.isEmpty()) return null;

        return disbursementRepository.findByLoanApplication(loanAppOpt.get()).orElse(null);
    }

}
