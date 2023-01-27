package com.neu.cloudapp.mapper;

import com.neu.cloudapp.dao.DocumentDao;
import com.neu.cloudapp.entity.Document;

public class Mapper {

    public static DocumentDao mapEntityToDao(Document document){
        DocumentDao documentDao = new DocumentDao();
        documentDao.setDoc_id(document.getDoc_id().toString());
        documentDao.setUser_id(document.getUser_id().getId().toString());
        documentDao.setFileName(document.getName());
        documentDao.setS3BucketPath(document.getS3_bucket_path());
        documentDao.setDateCreated(document.getDate_created());
        return documentDao;
    }


}
