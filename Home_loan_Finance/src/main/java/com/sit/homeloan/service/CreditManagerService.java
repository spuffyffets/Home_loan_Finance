package com.sit.homeloan.service;

import com.sit.homeloan.dto.LoanApplicationDetailsDTO;
import com.sit.homeloan.dto.LoanWithDocumentsDTO;
import com.sit.homeloan.model.Document;
import com.sit.homeloan.model.LoanApplication;
import com.sit.homeloan.model.SanctionLetter;

import java.util.List;

public interface CreditManagerService {

	
	
	
	
	List<LoanApplicationDetailsDTO> getApplicationsWithDocumentsSubmitted();
	
	LoanWithDocumentsDTO getLoanWithDocuments(Long loanAppId);
	
	
	void updateVerificationStatus(Long documentId, String status);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	void evaluateLoanApplication(Long loanApplicationId);

	SanctionLetter generateSanctionLetter(Long loanApplicationId, SanctionLetter input);

	SanctionLetter getSanctionLetter(Long loanApplicationId);

	
	
	
	
	


}
