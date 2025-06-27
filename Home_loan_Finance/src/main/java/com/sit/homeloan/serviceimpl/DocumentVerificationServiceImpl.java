package com.sit.homeloan.serviceimpl;

import com.sit.homeloan.enums.VerificationStatus;
import com.sit.homeloan.model.Document;
import com.sit.homeloan.repository.DocumentRepository;
import com.sit.homeloan.service.DocumentVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentVerificationServiceImpl implements DocumentVerificationService {

    private final DocumentRepository documentRepository;

    @Override
    public List<Document> getDocumentsByCustomerId(Long customerId) {
        return documentRepository.findByCustomerId(customerId);
    }

    @Override
    public String updateVerificationStatus(Long documentId, VerificationStatus status) {
        Optional<Document> optional = documentRepository.findById(documentId);
        if (optional.isEmpty()) {
            throw new RuntimeException("Document not found");
        }

        Document doc = optional.get();
        doc.setVerificationStatus(status);
        documentRepository.save(doc);
        return "Document ID " + documentId + " status updated to " + status;
    }
}
