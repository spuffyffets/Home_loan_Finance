package com.sit.homeloan.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sit.homeloan.dto.CustomerProfileDTO;
import com.sit.homeloan.dto.LoanApplicationDTO;
import com.sit.homeloan.dto.LoanApplicationRequestDTO;
import com.sit.homeloan.enums.DocumentType;
import com.sit.homeloan.model.Customer;
import com.sit.homeloan.model.Document;
import com.sit.homeloan.model.LoanApplication;

public interface CustomerService {

	CustomerProfileDTO getCustomerProfileDTO(String email);

	String applyForLoan(LoanApplicationRequestDTO requestDTO);

	String deleteLoanApplication(Long applicationId, String email);

	String uploadDocument(MultipartFile file, String email, DocumentType documentType);

	List<LoanApplicationDTO> getMyLoanApplications(String email);

	List<Document> getMyDocuments(String email);
}
