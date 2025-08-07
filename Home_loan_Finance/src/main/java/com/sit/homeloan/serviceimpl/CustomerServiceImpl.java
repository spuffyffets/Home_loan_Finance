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

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
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

//	private final String uploadDir = "C:/uploads";

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

		if (!customerOpt.isPresent()) {
			return "Customer not found!";
		}

		try {

			byte[] fileBytes = file.getBytes();

			Document doc = new Document();
			doc.setCustomer(customerOpt.get());
			doc.setFileData(fileBytes);
			doc.setFileName(file.getOriginalFilename());
			doc.setFileType(file.getContentType());
			doc.setUploadDate(LocalDate.now());
			doc.setDocumentType(documentType);
			doc.setVerificationStatus(VerificationStatus.PENDING);

			documentRepository.save(doc);

			List<LoanApplication> applications = loanApplicationRepository.findByCustomer_User_Email(email);

			if (applications != null && !applications.isEmpty()) {

				LoanApplication latest = applications.get(applications.size() - 1);

				List<Document> uploadedDocs = documentRepository.findByCustomer_User_Email(email);

				Set<DocumentType> uploadedTypes = new HashSet<>();

				for (Document d : uploadedDocs) {
					uploadedTypes.add(d.getDocumentType());
				}

				if (!uploadedTypes.isEmpty()) {
					if (latest.getApplicationStatus().ordinal() < ApplicationStatus.DOCUMENT_SUBMITTED.ordinal()) {
						latest.setApplicationStatus(ApplicationStatus.DOCUMENT_SUBMITTED);
					}
				}

				loanApplicationRepository.save(latest);
			}

			return "Document uploaded successfully!";
		} catch (IOException e) {
			return "File upload failed!";
		}
	}

	@Override
	public ResponseEntity<Resource> downloadDocument(String fileName) {
		List<Document> documents = documentRepository.findAll();

		for (Document doc : documents) {
			if (doc.getFileName().equals(fileName)) {
				ByteArrayResource resource = new ByteArrayResource(doc.getFileData());

				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + doc.getFileName() + "\"")
						.header(HttpHeaders.CONTENT_TYPE, doc.getFileType()).body(resource);
			}
		}

		return ResponseEntity.notFound().build();
	}

	@Override
	public List<Document> getMyDocuments(String email) {
		return documentRepository.findByCustomer_User_Email(email);
	}

	@Override
	public List<LoanApplicationDTO> getMyLoanApplications(String email) {
		List<LoanApplication> applications = loanApplicationRepository.findByCustomer_User_Email(email);

		List<LoanApplicationDTO> dtoList = new ArrayList<>();

		for (LoanApplication app : applications) {
			LoanApplicationDTO dto = new LoanApplicationDTO();
			dto.setId(app.getId());
			dto.setApplicantName(app.getCustomer().getUser().getFullName());
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
