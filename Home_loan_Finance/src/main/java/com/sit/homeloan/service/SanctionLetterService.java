package com.sit.homeloan.service;

import com.sit.homeloan.model.SanctionLetter;

public interface SanctionLetterService {
    SanctionLetter generateSanctionLetter(Long loanApplicationId, SanctionLetter sanctionLetterData);
    SanctionLetter getByLoanApplicationId(Long loanApplicationId);
}
