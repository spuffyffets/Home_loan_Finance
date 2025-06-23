package com.sit.homeloan.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.sit.homeloan.dto.LoanApplicationDTO;
import com.sit.homeloan.model.LoanApplication;

public interface LoanApplicationService {
    LoanApplication uploadDocument(String applicationNumber, String documentType, MultipartFile file, boolean isValid) throws IOException;
    LoanApplicationDTO getApplicationByNumber(String applicationNumber);
}
