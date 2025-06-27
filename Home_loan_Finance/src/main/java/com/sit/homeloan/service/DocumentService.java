package com.sit.homeloan.service;

import com.sit.homeloan.enums.DocumentType;
import com.sit.homeloan.model.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {
    String uploadDocument(MultipartFile file, String email, DocumentType documentType);
    List<Document> getDocumentsByCustomerEmail(String email);
}
