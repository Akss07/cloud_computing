package com.neu.cloudapp.dao;

import java.util.UUID;

public class DocumentDao {
    private String doc_id;
    private String user_id;
    private String fileName;
    private String s3BucketPath;
    private String dateCreated;

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getS3BucketPath() {
        return s3BucketPath;
    }

    public void setS3BucketPath(String s3BucketPath) {
        this.s3BucketPath = s3BucketPath;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
