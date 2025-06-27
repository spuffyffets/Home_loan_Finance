package com.sit.homeloan.serviceimpl;

import com.sit.homeloan.enums.ApplicationStatus;
import com.sit.homeloan.model.LoanApplication;
import com.sit.homeloan.model.User;
import com.sit.homeloan.repository.LoanApplicationRepository;
import com.sit.homeloan.repository.UserRepository;
import com.sit.homeloan.service.LoanOfficerService;
import com.sit.homeloan.service.LoanStageHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanOfficerServiceImpl implements LoanOfficerService {

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanStageHistoryService loanStageHistoryService;

    @Override
    public List<LoanApplication> getAllPendingApplications() {
        return loanApplicationRepository.findByApplicationStatus(ApplicationStatus.PENDING);
    }

    @Override
    public String reviewCIBILAndRequestDocuments(Long applicationId, String officerEmail) {
        Optional<LoanApplication> applicationOpt = loanApplicationRepository.findById(applicationId);
        if (applicationOpt.isEmpty()) return "Loan application not found.";

        LoanApplication app = applicationOpt.get();

        if (app.getApplicationStatus() != ApplicationStatus.PENDING)
            return "Application is not in pending state.";

        Optional<User> officerOpt = userRepository.findByEmail(officerEmail);
        if (officerOpt.isEmpty()) return "Officer not found.";

       
        app.setApplicationStatus(ApplicationStatus.REQUESTED_DOCUMENTS);
        loanApplicationRepository.save(app);

        
        loanStageHistoryService.logStage(
            app.getId(),
            officerOpt.get().getFullName(),
            officerOpt.get().getRole().name(),
            ApplicationStatus.REQUESTED_DOCUMENTS.name(),
            "CIBIL reviewed (" + app.getCibilScore() + "). Requested documents from customer."
        );

        return "CIBIL reviewed. Requested customer to upload documents.";
    }

}
