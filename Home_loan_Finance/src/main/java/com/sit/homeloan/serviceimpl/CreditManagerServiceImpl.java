package com.sit.homeloan.serviceimpl;

import com.sit.homeloan.enums.VerificationStatus;
import com.sit.homeloan.model.Document;
import com.sit.homeloan.repository.CreditManagerRepository;
import com.sit.homeloan.service.CreditManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CreditManagerServiceImpl implements CreditManagerService {

    @Autowired
    private CreditManagerRepository creditManagerRepository;

    @Override
    public List<Document> getDocumentsByCustomerId(Long customerId) {
        return creditManagerRepository.getByCustomer_Id(customerId);
    }
    @Override
    public void updateVerificationStatus(Long documentId, String status) {
        Document document = creditManagerRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found with ID: " + documentId));
        document.setVerificationStatus(VerificationStatus.valueOf(status.toUpperCase()));
        creditManagerRepository.save(document);
    }
}
