package com.neu.cloudapp.controller;

import com.neu.cloudapp.dao.DocumentDao;
import com.neu.cloudapp.entity.Document;
import com.neu.cloudapp.entity.User;
import com.neu.cloudapp.exception.ResourceNotFoundException;;
import com.neu.cloudapp.service.DocumentService;
import com.timgroup.statsd.StatsDClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/v1/documents")
public class DocumentController {
    private static final String DELETE_MESSAGE = "Deleted the file successfully";
    @Autowired
    DocumentService documentService;

    @Autowired
    StatsDClient statsDClient;
    @PostMapping("/")
    public ResponseEntity<DocumentDao> uploadDocument(@RequestHeader("Authorization") String authHeader,
                                                  @RequestParam("imageFile") MultipartFile file) throws IOException {
        statsDClient.increment("APIPostUploadDocumentCount");
        return ResponseEntity.ok().body(documentService.uploadDocument(authHeader,file));
    }

    @GetMapping("/{doc_id}")
    public ResponseEntity<DocumentDao> getDocument(@RequestHeader("Authorization") String authHeader,@PathVariable UUID doc_id) throws ResourceNotFoundException{
        statsDClient.increment("APIGetDocumentCount");
        DocumentDao document = documentService.getDocument(authHeader,doc_id);
        return ResponseEntity.ok().body(document);
    }

    @DeleteMapping("/{doc_id}")
    public String deleteDocument(@RequestHeader("Authorization") String authHeader,@PathVariable UUID doc_id){
        statsDClient.increment("APIDeleteDocumentCount");
        documentService.deleteDocument(authHeader,doc_id);
        return DELETE_MESSAGE;
    }
    @GetMapping("/")
    public ResponseEntity<List<DocumentDao>> getAllDocument(@RequestHeader("Authorization") String authHeader){
        statsDClient.increment("APIGetAllDocumentCount");
        List<DocumentDao> documentDao = documentService.getAllDocument(authHeader);
        return ResponseEntity.ok().body(documentDao);
    }
    private HttpHeaders formAuthHeader(User user) {
        HttpHeaders responseHeaders = new HttpHeaders();
        String encoding = Base64.getEncoder().encodeToString((user.getUser_name()
                + ":" + user.getPassword()).getBytes(StandardCharsets.UTF_8));
        responseHeaders.set(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
        return responseHeaders;
    }
}
