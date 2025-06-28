package com.sit.homeloan.serviceimpl;

import com.sit.homeloan.model.LoanApplication;
import com.sit.homeloan.model.SanctionLetter;
import com.sit.homeloan.repository.LoanApplicationRepository;
import com.sit.homeloan.repository.SanctionLetterRepository;
import com.sit.homeloan.service.SanctionLetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SanctionLetterServiceImpl implements SanctionLetterService {

    @Autowired
    private SanctionLetterRepository sanctionLetterRepository;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Override
    public SanctionLetter generateSanctionLetter(Long loanApplicationId, SanctionLetter input) {
        Optional<LoanApplication> optionalLoan = loanApplicationRepository.findById(loanApplicationId);

        if (optionalLoan.isPresent()) {
            LoanApplication loanApp = optionalLoan.get();

            SanctionLetter letter = new SanctionLetter();
            letter.setLoanApplication(loanApp);
            letter.setIssueDate(LocalDate.now());
            letter.setSanctionedAmount(input.getSanctionedAmount());
            letter.setInterestRate(input.getInterestRate());
            letter.setTenureInMonths(input.getTenureInMonths());
            letter.setEmiScheduleFileUrl(input.getEmiScheduleFileUrl());

            // Static values set by default in entity

            return sanctionLetterRepository.save(letter);
        } else {
            throw new RuntimeException("Loan Application not found with ID: " + loanApplicationId);
        }
    }

    @Override
    public SanctionLetter getByLoanApplicationId(Long loanApplicationId) {
        return sanctionLetterRepository.findByLoanApplication_Id(loanApplicationId);
    }
}
