package com.sit.homeloan.serviceimpl;

import com.sit.homeloan.enums.ApplicationStatus;
import com.sit.homeloan.enums.VerificationStatus;
import com.sit.homeloan.model.CreditEvaluation;
import com.sit.homeloan.model.Document;
import com.sit.homeloan.model.LoanApplication;
import com.sit.homeloan.model.SanctionLetter;
import com.sit.homeloan.repository.CreditEvaluationRepository;
import com.sit.homeloan.repository.CreditManagerRepository;
import com.sit.homeloan.repository.LoanApplicationRepository;
import com.sit.homeloan.repository.SanctionLetterRepository;
import com.sit.homeloan.service.CreditManagerService;
import com.sit.homeloan.service.LoanStageHistoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CreditManagerServiceImpl implements CreditManagerService {

    @Autowired
    private CreditManagerRepository creditManagerRepository;
    
    @Autowired
    private LoanApplicationRepository loanApplicationRepository;
    
    @Autowired
    private LoanStageHistoryService loanStageHistoryService;

    @Autowired
    private CreditEvaluationRepository creditEvaluationRepository;


    @Autowired
    private SanctionLetterRepository sanctionLetterRepository;


    @Override
    public List<Document> getDocumentsByCustomerId(Long customerId) {
        return creditManagerRepository.getByCustomer_Id(customerId);
    }
    @Override
    public void updateVerificationStatus(Long documentId, String status) {
        Document document = creditManagerRepository.findById(documentId).orElse(null);

        if (document == null) {
            throw new RuntimeException("Document not found with ID: " + documentId);
        }

        document.setVerificationStatus(VerificationStatus.valueOf(status.toUpperCase()));
        creditManagerRepository.save(document);
        LoanApplication app = document.getCustomer().getLoanApplications().get(0); 

        loanStageHistoryService.logStage(
            app.getId(),
            "Credit Manager", 
            "CREDIT_MANAGER",
            ApplicationStatus.DOCUMENT_SUBMITTED.name(),
            "Verified document: " + document.getDocumentType()
        );

    }
    
    @Override
    public void evaluateLoanApplication(Long loanAppId) {
        Optional<LoanApplication> loanOpt = loanApplicationRepository.findById(loanAppId);
        if (!loanOpt.isPresent()) {
            throw new RuntimeException("Loan Application not found with ID: " + loanAppId);
        }

        LoanApplication loanApp = loanOpt.get();

        Long customerId = loanApp.getCustomer().getId();
        List<Document> documentList = creditManagerRepository.getByCustomer_Id(customerId);

        boolean allVerified = true;
        for (int i = 0; i < documentList.size(); i++) {
            Document doc = documentList.get(i);
            if (!doc.getVerificationStatus().equals(VerificationStatus.VERIFIED)) {
                allVerified = false;
                break;
            }
        }

        if (!allVerified) {
            throw new RuntimeException("All documents must be VERIFIED before evaluation");
        }

        
        CreditEvaluation evaluation = new CreditEvaluation();
        evaluation.setLoanApplication(loanApp);

        double income = loanApp.getCustomer().getMonthlyIncome();
        double loanAmount = loanApp.getLoanAmount();
        double dtiRatio = loanAmount / (income * 12); 

        evaluation.setDebtToIncomeRatio(dtiRatio);
        evaluation.setApprovedAmount(loanAmount); 
        evaluation.setInterestRate(10.5); 
        evaluation.setEvaluationRemarks("Eligible based on verified documents and income.");
        evaluation.setEvaluationStatus("APPROVED");

        creditEvaluationRepository.save(evaluation);

        
        loanApp.setApplicationStatus(ApplicationStatus.EVALUATED);
        loanApp.setCreditEvaluation(evaluation); 
        loanApplicationRepository.save(loanApp);

        
        loanStageHistoryService.logStage(
            loanApp.getId(),
            "Credit Manager",
            "CREDIT_MANAGER",
            ApplicationStatus.EVALUATED.name(),
            "Loan eligibility evaluated by credit manager."
        );
    }


    @Override
    public SanctionLetter generateSanctionLetter(Long loanAppId, SanctionLetter input) {
        Optional<LoanApplication> loanOpt = loanApplicationRepository.findById(loanAppId);
        if (!loanOpt.isPresent()) {
            throw new RuntimeException("Loan Application not found with ID: " + loanAppId);
        }

        LoanApplication loanApp = loanOpt.get();

        SanctionLetter letter = new SanctionLetter();
        letter.setLoanApplication(loanApp);
        letter.setIssueDate(LocalDate.now());
        letter.setSanctionedAmount(input.getSanctionedAmount());
        letter.setInterestRate(input.getInterestRate());
        letter.setTenureInMonths(input.getTenureInMonths());
        letter.setEmiScheduleFileUrl(input.getEmiScheduleFileUrl());

        SanctionLetter savedLetter = sanctionLetterRepository.save(letter);

        loanApp.setSanctionLetter(savedLetter);
        loanApp.setApplicationStatus(ApplicationStatus.SANCTIONED);
        loanApplicationRepository.save(loanApp);
        LoanApplication app = loanApplicationRepository.findById(loanAppId).orElse(null);
        if (app != null) {
            app.setApplicationStatus(ApplicationStatus.SANCTIONED);
            loanApplicationRepository.save(app);

            loanStageHistoryService.logStage(
                app.getId(),
                "Credit Manager",
                "CREDIT_MANAGER",
                ApplicationStatus.SANCTIONED.name(),
                "Sanction letter issued."
            );
        }


        return savedLetter;
        
    }

    @Override
    public SanctionLetter getSanctionLetter(Long loanAppId) {
        return sanctionLetterRepository.findByLoanApplication_Id(loanAppId);
    }
    
}

