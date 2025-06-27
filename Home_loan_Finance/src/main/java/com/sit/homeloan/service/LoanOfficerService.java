package com.sit.homeloan.service;

import com.sit.homeloan.model.LoanApplication;
import java.util.List;

public interface LoanOfficerService {
    List<LoanApplication> getAllPendingApplications();
    String reviewCIBILAndRequestDocuments(Long applicationId, String officerEmail);
}
