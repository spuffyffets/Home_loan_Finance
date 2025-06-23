package com.sit.homeloan.serviceimpl;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sit.homeloan.dto.LoanApplicationDTO;
import com.sit.homeloan.model.LoanApplication;
import com.sit.homeloan.repository.LoanApplicationRepository;
import com.sit.homeloan.service.LoanApplicationService;

@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {

    @Autowired
    private LoanApplicationRepository repository;

    @Override
    public LoanApplication uploadDocument(String applicationNumber, String documentType, MultipartFile file, boolean isValid) throws IOException {
        // Get application or create new
        LoanApplication application = repository.findByApplicationNumber(applicationNumber)
                .orElse(new LoanApplication(applicationNumber));

        // Set file data and validity based on document type
        byte[] fileData = file.getBytes();

        switch (documentType) {
            case "identityProof" -> {
                application.setIdentityProof(fileData);
                application.setIsIdentityProofValid(isValid);
            }
            case "addressProof" -> {
                application.setAddressProof(fileData);
                application.setIsAddressProofValid(isValid);
            }
            case "photograph" -> {
                application.setPhotograph(fileData);
                application.setIsPhotographValid(isValid);
            }
            case "incomeProof" -> {
                application.setIncomeProof(fileData);
                application.setIsIncomeProofValid(isValid);
            }
            case "bankStatement" -> {
                application.setBankStatement(fileData);
                application.setIsBankStatementValid(isValid);
            }
            case "propertyDocuments" -> {
                application.setPropertyDocuments(fileData);
                application.setIsPropertyDocumentsValid(isValid);
            }
            case "employmentProof" -> {
                application.setEmploymentProof(fileData);
                application.setIsEmploymentProofValid(isValid);
            }
            case "signature" -> {
                application.setSignature(fileData);
                application.setIsSignatureValid(isValid);
            }
            default -> throw new IllegalArgumentException("Invalid document type");
        }

        return repository.save(application);
    }

    @Override
    public LoanApplicationDTO getApplicationByNumber(String applicationNumber) {
        LoanApplication application = repository.findByApplicationNumber(applicationNumber)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        return mapToDTO(application);
    }

    private LoanApplicationDTO mapToDTO(LoanApplication application) {
        return new LoanApplicationDTO(
                application.getApplicationNumber(),

                toBase64(application.getIdentityProof()), application.getIsIdentityProofValid(),
                toBase64(application.getAddressProof()), application.getIsAddressProofValid(),
                toBase64(application.getPhotograph()), application.getIsPhotographValid(),
                toBase64(application.getIncomeProof()), application.getIsIncomeProofValid(),
                toBase64(application.getBankStatement()), application.getIsBankStatementValid(),
                toBase64(application.getPropertyDocuments()), application.getIsPropertyDocumentsValid(),
                toBase64(application.getEmploymentProof()), application.getIsEmploymentProofValid(),
                toBase64(application.getSignature()), application.getIsSignatureValid()
        );
    }

    private String toBase64(byte[] data) {
        return (data != null) ? Base64.getEncoder().encodeToString(data) : null;
    }
}
