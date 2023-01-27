package com.neu.cloudapp.uploader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Component
public class DocumentUploader {
    Logger logger = LoggerFactory.getLogger(DocumentUploader.class);
    private static final String accessKey = "<put your access key>";
    private static final String secretKey = "<put your access key>";
    AmazonS3 amazonS3;

    @Value("${images.bucket.name}")
    private String imageBucketName;

    public DocumentUploader() {
        amazonS3 =  AmazonS3ClientBuilder
                .standard()
                .withRegion(Regions.US_EAST_1)
//                .withCredentials(new AWSStaticCredentialsProvider(new
//                        BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }

    public String uploadDocumentToS3(String s3Key, MultipartFile multipartFile){
        String s3ObjectName=null;
        logger.info("File upload in progress.");
        try {
            final File file = convertMultiPartFileToFile(multipartFile);
            s3ObjectName = uploadFileToS3Bucket(file, s3Key);
            logger.info("File upload is completed.");
            file.delete();  // To remove the file locally created in the project folder.

        } catch (final AmazonServiceException ex) {
            logger.info("File upload is failed.");
            logger.error("Error= {} while uploading file.", ex.getMessage());
        }
        long imageUploadS3Complete = System.currentTimeMillis();
        return s3ObjectName;
    }

    public void deleteDocumentFromS3(String s3Key) {
        DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(imageBucketName)
                .withKeys(s3Key)
                .withQuiet(false);
        DeleteObjectsResult delObjRes = amazonS3.deleteObjects(multiObjectDeleteRequest);
        int successfulDeletes = delObjRes.getDeletedObjects().size();
        logger.info(successfulDeletes + " objects successfully deleted.");
    }

    private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
        final File file = new File(multipartFile.getOriginalFilename());
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (final IOException ex) {
            logger.error("Error converting the multi-part file to file= ", ex.getMessage(), ex);
        }
        return file;
    }

    private String uploadFileToS3Bucket(final File file, final String s3Key) {
        logger.info("Uploading file with name= " + s3Key);
        final PutObjectRequest putObjectRequest = new PutObjectRequest(imageBucketName, s3Key, file);
        amazonS3.putObject(putObjectRequest);
        return s3Key;
    }
}
