package com.sit.homeloan.service;

import com.sit.homeloan.dto.LoanApplicationDTO;
import com.sit.homeloan.dto.LoanApplicationDetailsDTO;
import com.sit.homeloan.dto.LoanApplicationforsanctionDTO;
import com.sit.homeloan.dto.LoanWithDocumentsDTO;
import com.sit.homeloan.model.Document;
import com.sit.homeloan.model.LoanApplication;
import com.sit.homeloan.model.SanctionLetter;

import java.util.List;

public interface CreditManagerService {

	List<LoanApplicationDetailsDTO> getApplicationsWithDocumentsSubmitted();

	LoanWithDocumentsDTO getLoanWithDocuments(Long loanAppId);

	void updateVerificationStatus(Long documentId, String status);
	
	List<LoanApplicationDTO> getApplicationsReadyForEvaluation();

	void evaluateLoanApplication(Long loanApplicationId);
	 List<LoanApplicationforsanctionDTO> getEvaluatedApplications();

	SanctionLetter generateSanctionLetter(Long loanApplicationId, SanctionLetter input);

	SanctionLetter getSanctionLetter(Long loanApplicationId);

}
