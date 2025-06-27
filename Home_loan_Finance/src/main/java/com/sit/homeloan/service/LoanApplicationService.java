package com.sit.homeloan.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import com.sit.homeloan.dto.LoanApplicationDTO;
import com.sit.homeloan.model.LoanApplication;

public interface LoanApplicationService {

    LoanApplication uploadDocument(Long id, String documentType, MultipartFile file, boolean isValid) throws IOException;

    LoanApplicationDTO getApplicationById(Long id);
}
