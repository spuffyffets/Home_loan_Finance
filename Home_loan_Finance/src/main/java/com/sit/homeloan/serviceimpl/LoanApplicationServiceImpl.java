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
        LoanApplication app = repository.findByApplicationNumber(applicationNumber)
                .orElseGet(() -> {
                    LoanApplication newApp = new LoanApplication();
                    newApp.setApplicationNumber(applicationNumber);
                    return newApp;
                });

        byte[] fileData = file.getBytes();

        switch (documentType) {
            case "identityProof" -> { app.setIdentityProof(fileData); app.setIsIdentityProofValid(isValid); }
            case "addressProof" -> { app.setAddressProof(fileData); app.setIsAddressProofValid(isValid); }
            case "photograph" -> { app.setPhotograph(fileData); app.setIsPhotographValid(isValid); }
            case "incomeProof" -> { app.setIncomeProof(fileData); app.setIsIncomeProofValid(isValid); }
            case "bankStatement" -> { app.setBankStatement(fileData); app.setIsBankStatementValid(isValid); }
            case "propertyDocuments" -> { app.setPropertyDocuments(fileData); app.setIsPropertyDocumentsValid(isValid); }
            case "employmentProof" -> { app.setEmploymentProof(fileData); app.setIsEmploymentProofValid(isValid); }
            case "signature" -> { app.setSignature(fileData); app.setIsSignatureValid(isValid); }
            default -> throw new IllegalArgumentException("Invalid document type");
        }

        return repository.save(app);
    }

    @Override
    public LoanApplicationDTO getApplicationByNumber(String applicationNumber) {
        LoanApplication app = repository.findByApplicationNumber(applicationNumber)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        return mapToDTO(app);
    }

    private LoanApplicationDTO mapToDTO(LoanApplication app) {
        return new LoanApplicationDTO(
                app.getApplicationNumber(),

                toBase64(app.getIdentityProof()), app.getIsIdentityProofValid(),
                toBase64(app.getAddressProof()), app.getIsAddressProofValid(),
                toBase64(app.getPhotograph()), app.getIsPhotographValid(),
                toBase64(app.getIncomeProof()), app.getIsIncomeProofValid(),
                toBase64(app.getBankStatement()), app.getIsBankStatementValid(),
                toBase64(app.getPropertyDocuments()), app.getIsPropertyDocumentsValid(),
                toBase64(app.getEmploymentProof()), app.getIsEmploymentProofValid(),
                toBase64(app.getSignature()), app.getIsSignatureValid()
        );
    }

    private String toBase64(byte[] data) {
        return data != null ? Base64.getEncoder().encodeToString(data) : null;
    }
}
