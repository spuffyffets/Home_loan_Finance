package com.sit.homeloan.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class LoanApplication {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;



	@Lob
	private byte[] identityProof;
	private Boolean isIdentityProofValid;

	@Lob
	private byte[] addressProof;
	private Boolean isAddressProofValid;

	@Lob
	private byte[] photograph;
	private Boolean isPhotographValid;

	@Lob
	private byte[] incomeProof;
	private Boolean isIncomeProofValid;

	@Lob
	private byte[] bankStatement;
	private Boolean isBankStatementValid;

	@Lob
	private byte[] propertyDocuments;
	private Boolean isPropertyDocumentsValid;

	@Lob
	private byte[] employmentProof;
	private Boolean isEmploymentProofValid;

	@Lob
	private byte[] signature;
	private Boolean isSignatureValid;

	// getters & setters
}
