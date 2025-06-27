package com.sit.homeloan.dto;

import lombok.Data;

@Data
public class LoanApplicationRequestDTO {
    private String email;
    private String panNumber;
    private String aadhaarNumber;
    private String address;
    private String employmentType;
    private String employerName;
    private Double monthlyIncome;

    private Double loanAmount;
    private Integer loanTenureInMonths;
    private String loanPurpose;
}
