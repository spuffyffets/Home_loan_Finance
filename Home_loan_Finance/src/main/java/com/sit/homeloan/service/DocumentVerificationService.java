package com.sit.homeloan.service;

import com.sit.homeloan.enums.VerificationStatus;
import com.sit.homeloan.model.Document;

import java.util.List;

public interface DocumentVerificationService {
    List<Document> getDocumentsByCustomerId(Long customerId);
    String updateVerificationStatus(Long documentId, VerificationStatus status);
}
