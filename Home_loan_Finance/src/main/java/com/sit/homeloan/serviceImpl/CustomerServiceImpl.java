package com.sit.homeloan.serviceImpl;

import com.sit.homeloan.dto.LoanApplicationRequestDTO;
import com.sit.homeloan.enums.ApplicationStatus;
import com.sit.homeloan.enums.VerificationStatus;
import com.sit.homeloan.model.Customer;
import com.sit.homeloan.model.Document;
import com.sit.homeloan.model.LoanApplication;
import com.sit.homeloan.repository.CustomerRepository;
import com.sit.homeloan.repository.DocumentRepository;
import com.sit.homeloan.repository.LoanApplicationRepository;
import com.sit.homeloan.service.CustomerService;
import com.sit.homeloan.service.LoanStageHistoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private LoanStageHistoryService loanStageHistoryService;

    @Override
    public Customer getCustomerProfile(String email) {
        return customerRepository.findByUserEmail(email).orElse(null);
    }

    @Override
    public String applyForLoan(LoanApplicationRequestDTO dto) {
        Optional<Customer> customerOpt = customerRepository.findByUserEmail(dto.getEmail());
        if (customerOpt.isEmpty()) {
            return "Customer not found.";
        }

        Customer customer = customerOpt.get();

       
        List<LoanApplication> existingApplications = loanApplicationRepository.findByCustomer_User_Email(dto.getEmail());
        if (!existingApplications.isEmpty()) {
            return "You have already applied for a loan.";
        }

        
        if (customer.getPanNumber() == null || customer.getPanNumber().isEmpty()) {
            customer.setPanNumber(dto.getPanNumber());
        }
        if (customer.getAadhaarNumber() == null || customer.getAadhaarNumber().isEmpty()) {
            customer.setAadhaarNumber(dto.getAadhaarNumber());
        }
        customer.setAddress(dto.getAddress());
        customer.setEmploymentType(dto.getEmploymentType());
        customer.setEmployerName(dto.getEmployerName());
        customer.setMonthlyIncome(dto.getMonthlyIncome());
        customerRepository.save(customer);

        
        if (!dto.getPanNumber().equals(customer.getPanNumber())) {
            return "PAN verification failed.";
        }

        
        double randomCibil = 650 + Math.random() * 200;

        LoanApplication application = new LoanApplication();
        application.setCustomer(customer);
        application.setApplicationDate(LocalDate.now());
        application.setApplicationStatus(ApplicationStatus.PENDING);
        application.setCibilScore(randomCibil);
        application.setLoanAmount(dto.getLoanAmount());
        application.setLoanTenureInMonths(dto.getLoanTenureInMonths());
        application.setLoanPurpose(dto.getLoanPurpose());

        loanApplicationRepository.save(application);

        loanStageHistoryService.logStage(
            application.getId(),
            customer.getUser().getFullName(),
            customer.getUser().getRole().name(),
            ApplicationStatus.PENDING.name(),
            "Loan applied with CIBIL: " + randomCibil
        );

        return "Loan application submitted. CIBIL score: " + (int) randomCibil;
    }




    @Override
    public String uploadDocuments(String email, List<Document> documents) {
        Optional<Customer> customerOpt = customerRepository.findByUserEmail(email);
        if (customerOpt.isEmpty()) {
            return "Customer not found.";
        }

        Customer customer = customerOpt.get();

        for (Document doc : documents) {
            doc.setCustomer(customer);
            doc.setUploadDate(LocalDate.now());
            doc.setVerificationStatus(VerificationStatus.PENDING);
            documentRepository.save(doc);
        }

        return documents.size() + " document(s) uploaded successfully.";
    }

    @Override
    public List<LoanApplication> getMyLoanApplications(String email) {
        return loanApplicationRepository.findByCustomer_User_Email(email);
    }

    @Override
    public List<Document> getMyDocuments(String email) {
        return documentRepository.findByCustomer_User_Email(email);
    }
    
    @Override
    public String deleteLoanApplication(Long applicationId, String email) {
        Optional<LoanApplication> applicationOpt = loanApplicationRepository.findById(applicationId);
        if (applicationOpt.isEmpty()) {
            return "Loan application not found.";
        }

        LoanApplication application = applicationOpt.get();

        if (!application.getCustomer().getUser().getEmail().equals(email)) {
            return "Unauthorized deletion attempt.";
        }

        loanApplicationRepository.delete(application);
        return "Loan application deleted successfully.";
    }


    
    
    
}
