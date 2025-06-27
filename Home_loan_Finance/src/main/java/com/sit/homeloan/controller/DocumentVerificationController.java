package com.sit.homeloan.controller;

import com.sit.homeloan.enums.VerificationStatus;
import com.sit.homeloan.model.Document;
import com.sit.homeloan.service.DocumentVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/credit-manager/documents")
@RequiredArgsConstructor
public class DocumentVerificationController {

    private final DocumentVerificationService verificationService;

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Document>> getDocumentsByCustomer(@PathVariable Long customerId) {
        List<Document> docs = verificationService.getDocumentsByCustomerId(customerId);
        return docs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(docs);
    }

    @PutMapping("/{documentId}/verify")
    public ResponseEntity<String> verifyDocument(@PathVariable Long documentId,
                                                 @RequestParam VerificationStatus status) {
        return ResponseEntity.ok(verificationService.updateVerificationStatus(documentId, status));
    }
}
