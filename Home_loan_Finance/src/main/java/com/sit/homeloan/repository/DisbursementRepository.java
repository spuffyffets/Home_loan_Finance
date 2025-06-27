package com.sit.homeloan.repository;

import com.sit.homeloan.model.Disbursement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisbursementRepository extends JpaRepository<Disbursement, Long> {
}
