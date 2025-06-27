package com.sit.homeloan.service;

import com.sit.homeloan.model.Document;
import java.util.List;

public interface CreditManagerService {
	 List<Document> getDocumentsByCustomerId(Long customerId);
    void updateVerificationStatus(Long documentId, String status);
}
