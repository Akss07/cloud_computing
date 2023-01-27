package com.neu.cloudapp.controller;

import com.neu.cloudapp.service.DynamoDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/verifyUserEmail")
public class VerifyUserController {

    private static final String message = "User is Verified" ;
    @Autowired
    DynamoDBService dynamoDBService;

    @PostMapping("/{email}/{token}")
    public ResponseEntity verifyUser(@PathVariable String email, @PathVariable String token){
       dynamoDBService.isVerify(email, token);
        return ResponseEntity.ok(message);
    }
}


