package com.sit.homeloan.repository;

import com.sit.homeloan.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByCustomerId(Long customerId);
=======
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByCustomer_User_Email(String email);
>>>>>>> branch 'main' of https://github.com/spuffyffets/Homo_loan_Finance.git
}
