package com.sit.homeloan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationDTO {
    private String applicationNumber;

    private String identityProof;  // Base64 format
    private Boolean isIdentityProofValid;

    private String addressProof;
    private Boolean isAddressProofValid;

    private String photograph;
    private Boolean isPhotographValid;

    private String incomeProof;
    private Boolean isIncomeProofValid;

    private String bankStatement;
    private Boolean isBankStatementValid;

    private String propertyDocuments;
    private Boolean isPropertyDocumentsValid;

    private String employmentProof;
    private Boolean isEmploymentProofValid;

    private String signature;
    private Boolean isSignatureValid;

    // Constructors, Getters, Setters
}
