package com.sit.homeloan.controller;

import com.sit.homeloan.model.SanctionLetter;
import com.sit.homeloan.service.SanctionLetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sanction-letter")
public class SanctionLetterController {

    @Autowired
    private SanctionLetterService sanctionLetterService;

    @PostMapping("/generate/{loanAppId}")
    public ResponseEntity<SanctionLetter> generateSanctionLetter(
            @PathVariable Long loanAppId,
            @RequestBody SanctionLetter request
    ) {
        SanctionLetter sanctionLetter = sanctionLetterService.generateSanctionLetter(loanAppId, request);
        return ResponseEntity.ok(sanctionLetter);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SanctionLetter> getSanctionLetter(@PathVariable Long id) {
        SanctionLetter letter = sanctionLetterService.getByLoanApplicationId(id);
        return ResponseEntity.ok(letter);
    }
}
