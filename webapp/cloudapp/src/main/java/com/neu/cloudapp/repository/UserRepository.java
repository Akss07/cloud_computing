package com.neu.cloudapp.repository;

import com.neu.cloudapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u WHERE u.user_name = :user_name")
    User findByUserName(@Param("user_name") String user_name);

    @Query("SELECT u FROM User u WHERE u.user_name = :user_name and u.id = :id")
    User findByUserNameAndId(@Param("user_name") String user_name, @Param("id") UUID id);

    @Query("SELECT u FROM User u WHERE u.user_name = :user_name")
    Optional<User> findByEmail(@Param("user_name") String user_name);
}
