package com.neu.cloudapp.controller;

import com.neu.cloudapp.entity.User;
import com.neu.cloudapp.exception.ResourceNotFoundException;
import com.neu.cloudapp.repository.UserRepository;
import com.neu.cloudapp.service.UserService;
import com.neu.cloudapp.validator.UserDetailsValidator;
import com.timgroup.statsd.StatsDClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/v2/account")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDetailsValidator userDetailsValidator;

    @Autowired
    StatsDClient statsDClient;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Validated @RequestBody User user){
        statsDClient.increment("APIUserCreateCount");
        long currentApiExecution = System.currentTimeMillis();
        userDetailsValidator.checkUserData(user.getFirst_name(), user.getLast_name());
        userDetailsValidator.checkEmail(user.getUser_name());
        userDetailsValidator.checkPassword(user.getPassword());
        User savedUser = userService.createUser(user);
        long endApiExecution = System.currentTimeMillis();
        statsDClient.recordExecutionTime("APIUserCreateTime", endApiExecution-currentApiExecution);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<User> getUserById(@RequestHeader("Authorization") String authHeader,@PathVariable UUID accountId )
            throws ResourceNotFoundException {
        statsDClient.increment("APIGetUserCount");
        long currentApiExecution = System.currentTimeMillis();
        User user = userService.getUser(authHeader, accountId);
        long endApiExecution = System.currentTimeMillis();
        statsDClient.recordExecutionTime("APIGetUserTime", endApiExecution-currentApiExecution);
        return ResponseEntity.ok().headers(formAuthHeader(user)).body(user);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<User> updateUser(@RequestHeader("Authorization") String authHeader, @Validated @RequestBody User userDetails, @PathVariable UUID accountId )
            throws ResourceNotFoundException {
        statsDClient.increment("APIUpdateUserCount");
        long currentApiExecution = System.currentTimeMillis();
        userDetailsValidator.checkUserData(userDetails.getFirst_name(), userDetails.getLast_name());
        userDetailsValidator.checkPassword(userDetails.getPassword());
        userDetailsValidator.checkOtherFields(userDetails);
        User user = userService.updateUser(authHeader, userDetails, accountId);
        long endApiExecution = System.currentTimeMillis();
        statsDClient.recordExecutionTime("APIUpdateUserTime", endApiExecution-currentApiExecution);
        return ResponseEntity.ok().headers(formAuthHeader(user)).body(user);
    }
    private HttpHeaders formAuthHeader(User user) {
        HttpHeaders responseHeaders = new HttpHeaders();
        String encoding = Base64.getEncoder().encodeToString((user.getUser_name()
                + ":" + user.getPassword()).getBytes(StandardCharsets.UTF_8));
        responseHeaders.set(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
        return responseHeaders;
    }
}
