package com.sit.homeloan.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    private User user;

    private String address;
    private String employmentType;
    private String employerName;
    private Double monthlyIncome;
    private String panNumber;
    private String aadhaarNumber;
    private String kycStatus;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<LoanApplication> loanApplications;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Document> documents;
}
