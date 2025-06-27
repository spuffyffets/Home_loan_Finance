package com.sit.homeloan.repository;

import com.sit.homeloan.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CreditManagerRepository extends JpaRepository<Document, Long> {
	
    List<Document> getByCustomer_Id( Long customerId);
}
