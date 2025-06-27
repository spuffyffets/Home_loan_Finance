package com.sit.homeloan.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loan_stage_histories")
public class LoanStageHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private LoanApplication loanApplication;

    private String updatedByRole;
    private String updatedByName;
    private String stage;
    private String remarks;
    private LocalDateTime updatedAt;
}
