package com.sit.homeloan.serviceimpl;

import com.sit.homeloan.dto.CustomerProfileDTO;
import com.sit.homeloan.dto.LoanApplicationDTO;
import com.sit.homeloan.dto.LoanApplicationRequestDTO;
import com.sit.homeloan.enums.ApplicationStatus;
import com.sit.homeloan.enums.DocumentType;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
	public CustomerProfileDTO getCustomerProfileDTO(String email) {
		Optional<Customer> customerOpt = customerRepository.findByUserEmail(email);
		if (customerOpt.isEmpty()) {
			return null;
		}

		Customer customer = customerOpt.get();
		CustomerProfileDTO dto = new CustomerProfileDTO();

		dto.setFullName(customer.getUser().getFullName());
		dto.setEmail(customer.getUser().getEmail());
		dto.setPhoneNumber(customer.getUser().getPhoneNumber());
		dto.setAddress(customer.getAddress());
		dto.setEmploymentType(customer.getEmploymentType());
		dto.setMonthlyIncome(customer.getMonthlyIncome());
		dto.setPanNumber(customer.getPanNumber());
		dto.setAadhaarNumber(customer.getAadhaarNumber());
		dto.setKycStatus(customer.getKycStatus());

		return dto;
	}

	@Override
	public String applyForLoan(LoanApplicationRequestDTO dto) {
		Optional<Customer> customerOpt = customerRepository.findByUserEmail(dto.getEmail());
		
		if (customerOpt.isEmpty()) {
			return "Customer not found.";
		}

		Customer customer = customerOpt.get();

		List<LoanApplication> existingApplications = loanApplicationRepository
				.findByCustomer_User_Email(dto.getEmail());
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
		customer.setBankAccountNumber(dto.getBankAccountNumber());
		customer.setAccountHolderName(dto.getAccountHolderName());
		customer.setIfscCode(dto.getIfscCode());
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

		loanStageHistoryService.logStage(application.getId(), customer.getUser().getFullName(),
				customer.getUser().getRole().name(), ApplicationStatus.PENDING.name(),
				"Loan applied with CIBIL: " + randomCibil);

		return "Loan application submitted. CIBIL score: " + (int) randomCibil;
	}

	@Override
	public String uploadDocument(MultipartFile file, String email, DocumentType documentType) {
		Optional<Customer> customerOpt = customerRepository.findByUserEmail(email);
		if (customerOpt.isEmpty()) {
			return "Customer not found.";
		}

		Customer customer = customerOpt.get();

		Document doc = new Document();
		doc.setCustomer(customer);
		doc.setDocumentType(documentType);
		doc.setFileName(file.getOriginalFilename());
		doc.setUploadDate(LocalDate.now());
		doc.setVerificationStatus(VerificationStatus.PENDING);
		documentRepository.save(doc);

		List<DocumentType> requiredDocs = Arrays.asList(DocumentType.AADHAAR, DocumentType.PAN,
				DocumentType.SALARY_SLIP, DocumentType.BANK_STATEMENT, DocumentType.ADDRESS_PROOF,
				DocumentType.EMPLOYMENT_PROOF, DocumentType.PROPERTY_DOCUMENT, DocumentType.PHOTO,
				DocumentType.SIGNATURE);

		List<Document> uploadedDocs = documentRepository.findByCustomer_User_Email(email);
		Set<DocumentType> uploadedTypes = uploadedDocs.stream().map(Document::getDocumentType)
				.collect(Collectors.toSet());

		boolean allUploaded = requiredDocs.stream().allMatch(uploadedTypes::contains);

		if (allUploaded) {
			List<LoanApplication> apps = loanApplicationRepository.findByCustomer_User_Email(email);
			if (!apps.isEmpty()) {
				LoanApplication app = apps.get(0);
				app.setApplicationStatus(ApplicationStatus.DOCUMENT_SUBMITTED);
				loanApplicationRepository.save(app);
			}
		}

		return documentType + " uploaded successfully.";
	}

	@Override
	public List<LoanApplicationDTO> getMyLoanApplications(String email) {
		List<LoanApplication> applications = loanApplicationRepository.findByCustomer_User_Email(email);
		List<LoanApplicationDTO> dtoList = new ArrayList<>();

		for (LoanApplication app : applications) {
			LoanApplicationDTO dto = new LoanApplicationDTO();
			dto.setId(app.getId());
			dto.setLoanAmount(app.getLoanAmount());
			dto.setLoanTenureInMonths(app.getLoanTenureInMonths());
			dto.setLoanPurpose(app.getLoanPurpose());
			dto.setApplicationStatus(app.getApplicationStatus().name());
			dto.setApplicationDate(app.getApplicationDate());
			dto.setCibilScore(app.getCibilScore());

			dtoList.add(dto);
		}

		return dtoList;
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

		if (!application.getCustomer().getUser().getEmail().equalsIgnoreCase(email)) {
			return "Unauthorized deletion attempt.";
		}

		loanApplicationRepository.delete(application);
		return "Loan application deleted successfully.";
	}

}
