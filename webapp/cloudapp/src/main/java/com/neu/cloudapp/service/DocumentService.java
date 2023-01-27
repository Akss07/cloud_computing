package com.neu.cloudapp.service;

import com.neu.cloudapp.dao.DocumentDao;
import com.neu.cloudapp.dao.UserCredentials;
import com.neu.cloudapp.entity.Document;
import com.neu.cloudapp.entity.User;
import com.neu.cloudapp.exception.BadRequestException;
import com.neu.cloudapp.exception.ResourceNotFoundException;
import com.neu.cloudapp.exception.UnauthorizedError;
import com.neu.cloudapp.mapper.Mapper;
import com.neu.cloudapp.repository.DocumentRepository;
import com.neu.cloudapp.repository.UserRepository;
import com.neu.cloudapp.security.AuthenticationProvider;
import com.neu.cloudapp.security.PasswordEncoder;
import com.neu.cloudapp.uploader.DocumentUploader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    @Autowired
    DocumentUploader documentUploader;

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    UserRepository userRepository;

    private final static Logger logger = LoggerFactory.getLogger(DocumentService.class);
    public DocumentDao uploadDocument(String authHeader, MultipartFile file){
        UserCredentials userCredentials = AuthenticationProvider.fetchCredentialsFromHeader(authHeader);
        Optional<User> user = userRepository.findByEmail(userCredentials.getUserName());
        if(user.isEmpty()){
            logger.error("User not found", userCredentials.getUserName());
            throw new ResourceNotFoundException("User not found " + userCredentials.getUserName());
        }

        Boolean authenticUser = PasswordEncoder.checkPassword(userCredentials.getPassword(), user.get().getPassword());
        if(!authenticUser){
            logger.error("Credentials Invalid");
            throw new UnauthorizedError("Credentials Invalid");
        }

        if(user.get().getIs_valid() == false){
            logger.error("In uploadDocument, user is not verified");
            throw new UnauthorizedError("User is not verified");
        }

        //upload document
        Document document = new Document();
        document.setName(file.getOriginalFilename());
        document.setUser_id(user.get());

        Optional<Document> existingDocumentEntity = documentRepository.findByKey(buildS3Key(user.get().getId(),
                file.getOriginalFilename()));

        if (existingDocumentEntity.isPresent()) {
            logger.error("Document with name already exists ");
            throw new BadRequestException("Document with name already exists", "document");
        }
        logger.info("Uploading document with id ", userCredentials.getUserName() );

        String s3ObjectPath = documentUploader.uploadDocumentToS3(buildS3Key(user.get().getId(),
                                file.getOriginalFilename()), file);
        document.setS3_bucket_path(s3ObjectPath);

        document.setDate_created(String.valueOf(new Timestamp(System.currentTimeMillis())));

         Document documents = documentRepository.save(document);
        logger.info("Document uploaded ", documents.getDoc_id() );
        DocumentDao documentDao = Mapper.mapEntityToDao(documents);
        return documentDao;
    }

    public DocumentDao getDocument(String authHeader,UUID doc_id){
        UserCredentials userCredentials = AuthenticationProvider.fetchCredentialsFromHeader(authHeader);
        User user=userRepository.findByUserName(userCredentials.getUserName());
        if(user == null){
            logger.error("User not found", userCredentials.getUserName());
            throw new ResourceNotFoundException("User not found " + userCredentials.getUserName() );
        }
        if(user.getIs_valid() == false){
            logger.error("In get document, user is not verified");
            throw new UnauthorizedError("User is not verified");
        }

        PasswordEncoder.checkPassword(userCredentials.getPassword(), user.getPassword());
        logger.info("Getting Document with id ", doc_id);
        Document document = documentRepository.findByDoc_id(doc_id);
        if(document == null){
            logger.error("Document with this id not found", doc_id);
            throw new ResourceNotFoundException("Document with this id not found " + doc_id );
        }
        logger.info("Document found ", doc_id);
        DocumentDao documentDao = Mapper.mapEntityToDao(document);
        return documentDao;
    }

    public String deleteDocument(String authHeader,UUID doc_id){
        UserCredentials userCredentials = AuthenticationProvider.fetchCredentialsFromHeader(authHeader);
        User user=userRepository.findByUserName(userCredentials.getUserName());
        if(user == null){
            logger.error("User not found", userCredentials.getUserName());
            throw new ResourceNotFoundException("User not found " + userCredentials.getUserName() );
        }
        if(user.getIs_valid() == false){
            logger.error("In delete document, user is not verified");
            throw new UnauthorizedError("User is not verified");
        }

        PasswordEncoder.checkPassword(userCredentials.getPassword(), user.getPassword());
        Optional<Document> document = documentRepository.findById(doc_id);
        if(!document.isPresent()){
            logger.error("document not found with id : "+ doc_id);
            throw new ResourceNotFoundException("document not found with id : "+ doc_id);
        }
        if(!document.get().getUser_id().getId().equals(user.getId())){
            logger.error("User is not authorized to delete");
            throw new UnauthorizedError("User is not authorized to delete");
        }

        deleteDocument(document.get());
        logger.info("Document Deleted -  ", doc_id);
        String response="Document deleted";
        return response;
    }

    public List<DocumentDao> getAllDocument(String authHeader){
        UserCredentials userCredentials = AuthenticationProvider.fetchCredentialsFromHeader(authHeader);
        User user=userRepository.findByUserName(userCredentials.getUserName());
        if(user == null){
            logger.error("User not found", userCredentials.getUserName());
            throw new ResourceNotFoundException("User not found " + userCredentials.getUserName() );
        }
        if(user.getIs_valid() == false){
            logger.error("In uploadDocument, user is not verified");
            throw new UnauthorizedError("User is not verified");
        }
        PasswordEncoder.checkPassword(userCredentials.getPassword(), user.getPassword());
        List<Document> documentList = documentRepository.findByUser_id(user);
        if(documentList.isEmpty()){
            logger.error("Document with email id - " + userCredentials.getUserName() +" not found");
            throw new ResourceNotFoundException("Document with email id - " + userCredentials.getUserName() +" not found");
        }
        logger.info("Getting all the documents with the id -  ", userCredentials.getUserName());
        List<DocumentDao> document = documentList.stream().map(documents -> {
            DocumentDao documentDao = Mapper.mapEntityToDao(documents);
            return documentDao;
        }).collect(Collectors.toList());
        logger.info("documents with the id -  ", userCredentials.getUserName() + " found");
        return document;
    }

    private String buildS3Key(UUID id, String fileName) {
        return  id + "/" + fileName;
    }

    private void deleteDocument(Document document) {
        documentUploader.deleteDocumentFromS3(document.getS3_bucket_path());
        //delete document
        documentRepository.delete(document);
    }


}
