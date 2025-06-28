//package com.sit.homeloan.controller;
//
//import com.sit.homeloan.enums.DocumentType;
//import com.sit.homeloan.model.Document;
//import com.sit.homeloan.service.DocumentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/documents")
//@CrossOrigin("*")
//public class DocumentController {
//
//    @Autowired
//    private DocumentService documentService;
//
//    @PostMapping("/upload-document")
//    public String uploadDocument(@RequestParam("file") MultipartFile file,
//                                 @RequestParam("email") String email,
//                                 @RequestParam("type") DocumentType documentType) {
//        return documentService.uploadDocument(file, email, documentType);
//    }
//
//
//    @GetMapping("/get-by-customer")
//    public List<Document> getDocuments(@RequestParam String email) {
//        return documentService.getDocumentsByCustomerEmail(email);
//    }
//}
