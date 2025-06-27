package com.sit.homeloan.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sit.homeloan.model.LoanApplication;
import java.util.List;


@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
     Optional<LoanApplication> findById(Long id);
}
