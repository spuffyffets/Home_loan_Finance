package com.sit.homeloan.service;

import java.util.List;

import com.sit.homeloan.dto.LoanApplicationRequestDTO;
import com.sit.homeloan.model.Customer;
import com.sit.homeloan.model.Document;
import com.sit.homeloan.model.LoanApplication;

public interface CustomerService {

	Customer getCustomerProfile(String email);

	String applyForLoan(LoanApplicationRequestDTO requestDTO);

	String deleteLoanApplication(Long applicationId, String email);

	String uploadDocuments(String email, List<Document> documents);

	List<LoanApplication> getMyLoanApplications(String email);

	List<Document> getMyDocuments(String email);
}
