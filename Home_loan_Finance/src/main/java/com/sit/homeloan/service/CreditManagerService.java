package com.sit.homeloan.service;

import com.sit.homeloan.model.Document;
import com.sit.homeloan.model.SanctionLetter;

import java.util.List;

public interface CreditManagerService {
	 List<Document> getDocumentsByCustomerId(Long customerId);
	 void evaluateLoanApplication(Long loanApplicationId);
	 
	 SanctionLetter generateSanctionLetter(Long loanApplicationId, SanctionLetter input);
	 SanctionLetter getSanctionLetter(Long loanApplicationId);

    void updateVerificationStatus(Long documentId, String status);
}
