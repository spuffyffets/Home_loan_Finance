package com.sit.homeloan.serviceimpl;

import com.sit.homeloan.enums.ApplicationStatus;
import com.sit.homeloan.enums.DocumentType;
import com.sit.homeloan.enums.VerificationStatus;
import com.sit.homeloan.model.Customer;
import com.sit.homeloan.model.Document;
import com.sit.homeloan.model.LoanApplication;
import com.sit.homeloan.repository.CustomerRepository;
import com.sit.homeloan.repository.DocumentRepository;
import com.sit.homeloan.repository.LoanApplicationRepository;
import com.sit.homeloan.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;
    
    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private final String uploadDir = "C:/uploads"; 

    @Override
    public String uploadDocument(MultipartFile file, String email, DocumentType documentType) {
        Optional<Customer> customerOpt = customerRepository.findByUserEmail(email);
        if (customerOpt.isEmpty()) {
            return "Customer not found!";
        }

        try {
            
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs(); 
            }

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File dest = new File(uploadDir, fileName);
            file.transferTo(dest);

            Document doc = new Document();
            doc.setCustomer(customerOpt.get());
            doc.setFileUrl(dest.getAbsolutePath());
            doc.setUploadDate(LocalDate.now());
            doc.setDocumentType(documentType);
            doc.setVerificationStatus(VerificationStatus.PENDING);

            documentRepository.save(doc);
            
            List<LoanApplication> applications = loanApplicationRepository.findByCustomer_User_Email(email);
            if (!applications.isEmpty()) {
                LoanApplication latest = applications.get(applications.size() - 1); 
                latest.setApplicationStatus(ApplicationStatus.DOCUMENT_SUBMITTED);
                loanApplicationRepository.save(latest);
            }

            return "Document uploaded successfully!";
        } catch (IOException e) {
            return "File upload failed!";
        }
    }


    @Override
    public List<Document> getDocumentsByCustomerEmail(String email) {
        return documentRepository.findByCustomer_User_Email(email);
    }
}
