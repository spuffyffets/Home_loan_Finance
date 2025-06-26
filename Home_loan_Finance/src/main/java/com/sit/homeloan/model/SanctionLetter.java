package com.sit.homeloan.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sanction_letters")
public class SanctionLetter {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private LoanApplication loanApplication;

    private LocalDate issueDate;
    private Double sanctionedAmount;
    private Double interestRate;
    private Integer tenureInMonths;
    private String emiScheduleFileUrl;
}
