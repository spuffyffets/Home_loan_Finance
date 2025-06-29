package com.sit.homeloan.service;

import com.sit.homeloan.model.LoanApplication;
import java.util.List;

public interface LoanOfficerService {
    List<LoanApplication> getAllPendingApplications();
    String reviewCIBILDecision(Long applicationId, String officerEmail, boolean reject, String reasonIfRejected);

}
