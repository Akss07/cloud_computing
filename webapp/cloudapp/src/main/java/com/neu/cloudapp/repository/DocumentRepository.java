package com.neu.cloudapp.repository;

import com.neu.cloudapp.entity.Document;
import com.neu.cloudapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
    @Query("SELECT d FROM Document d WHERE d.s3_bucket_path = :s3_bucket_path")
    Optional<Document> findByKey(@Param("s3_bucket_path") String s3_bucket_path);
    @Query("SELECT d FROM Document d WHERE d.doc_id = :doc_id")
    Document findByDoc_id(@Param("doc_id") UUID doc_id);

    @Query("SELECT d FROM Document d WHERE d.user_id = :user_id")
    List<Document> findByUser_id(@Param("user_id") User user_id);

}
