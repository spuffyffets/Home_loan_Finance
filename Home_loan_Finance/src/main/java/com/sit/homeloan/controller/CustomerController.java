package com.sit.homeloan.controller;

import com.sit.homeloan.dto.CustomerProfileDTO;
import com.sit.homeloan.dto.LoanApplicationDTO;
import com.sit.homeloan.dto.LoanApplicationRequestDTO;
import com.sit.homeloan.enums.DocumentType;
import com.sit.homeloan.model.Customer;
import com.sit.homeloan.model.Document;
import com.sit.homeloan.model.LoanApplication;
import com.sit.homeloan.service.CustomerService;
import com.sit.homeloan.service.DocumentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/customer")
@CrossOrigin(origins = "http://localhost:4200")

public class CustomerController {

	@Autowired
	private CustomerService customerService;
	@Autowired
	private DocumentService documentService;

	@GetMapping("/profile")
	public CustomerProfileDTO getProfile(@RequestParam String email) {
		return customerService.getCustomerProfileDTO(email);
	}

	@PostMapping("/apply-loan")
	public String applyLoan(@RequestBody LoanApplicationRequestDTO requestDTO) {
		return customerService.applyForLoan(requestDTO);
	}

	@PostMapping("/upload-document")
	public String uploadDocument(@RequestParam("file") MultipartFile file, @RequestParam("email") String email,
			@RequestParam("type") DocumentType documentType) {
		return documentService.uploadDocument(file, email, documentType);
	}

	@GetMapping("/my-applications")
	public List<LoanApplicationDTO> getLoanApplications(@RequestParam String email) {
		return customerService.getMyLoanApplications(email);
	}

	@GetMapping("/my-documents")

	public List<Document> getDocuments(@RequestParam String email) {
		return customerService.getMyDocuments(email);
	}

	@DeleteMapping("/delete-loan")
	public String deleteLoanApplication(@RequestParam Long applicationId, @RequestParam String email) {
		return customerService.deleteLoanApplication(applicationId, email);
	}

}
