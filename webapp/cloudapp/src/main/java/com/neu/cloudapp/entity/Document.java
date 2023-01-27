package com.neu.cloudapp.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "document")
public class Document {
    @Id
    @Column(name = "doc_id", nullable = false)
    @GeneratedValue
    @Type(type="uuid-char")
    private UUID doc_id;
    @JoinColumn(name="user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user_id;

    @Column(name = "file_name", nullable = false)
    private String name;

    @Column(name = "date_created", nullable = false)
    private String date_created;

    @Column(name = "s3_bucket_path", unique = true)
    private String s3_bucket_path;

    public UUID getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(UUID doc_id) {
        this.doc_id = doc_id;
    }

    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getS3_bucket_path() {
        return s3_bucket_path;
    }

    public void setS3_bucket_path(String s3_bucket_path) {
        this.s3_bucket_path = s3_bucket_path;
    }
}
