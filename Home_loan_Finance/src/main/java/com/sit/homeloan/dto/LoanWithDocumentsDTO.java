package com.sit.homeloan.dto;

import java.util.List;


import com.sit.homeloan.model.Document;
import com.sit.homeloan.model.LoanApplication;

import lombok.Data;
@Data
public class LoanWithDocumentsDTO {

    private LoanApplication loan;
    private List<Document> documents;
}
