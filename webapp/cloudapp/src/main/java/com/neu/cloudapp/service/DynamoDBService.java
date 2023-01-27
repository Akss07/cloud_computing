package com.neu.cloudapp.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.neu.cloudapp.entity.User;
import com.neu.cloudapp.entity.VerifyUser;
import com.neu.cloudapp.exception.BadRequestException;
import com.neu.cloudapp.exception.ResourceNotFoundException;
import com.neu.cloudapp.exception.UnauthorizedError;
import com.neu.cloudapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Service
public class DynamoDBService {

   @Autowired
   DynamoDBMapper dynamoDBMapper;

   @Autowired
   UserRepository userRepository;


   private final static Logger logger = LoggerFactory.getLogger(DynamoDBService.class);
   public void saveInDynamoDB(String email, String token){
      logger.info("Inside saveInDynamoDB function");
      VerifyUser item = new VerifyUser();
      long currentTime = System.currentTimeMillis() / 1000L;
      logger.info("current time " + currentTime);
      try {
         long plus2Minutes = (System.currentTimeMillis() + (2 * 60 * 1000)) / 1000L;
         logger.info("Added 2 min in current Time " + plus2Minutes);

         item.setEmail(email);
         item.setToken(token);
         item.setTtl(plus2Minutes);
         dynamoDBMapper.save(item);
         logger.info("Values saved in Dynamo Db ");

      } catch (AmazonServiceException e) {
         throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage(), e);
      } catch (AmazonClientException e) {
         throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
      }
   }

   public void isVerify(String email, String token){
      VerifyUser verifyUser = dynamoDBMapper.load(VerifyUser.class, email);
      long current_time = System.currentTimeMillis() / 1000L;
      logger.info("time after sending verification email " + current_time);
      if(verifyUser == null ){
         logger.error("Token not found for user " + email);
         throw new ResourceNotFoundException("Token not found for user " + email);
      }

//      logger.info("user token " + token);

      if(!verifyUser.getToken().equals(token)){
         logger.error("token is not valid", "token");
         throw new BadRequestException("token is not valid", "token");
      }

      long ttl = verifyUser.getTtl();
      long time_diff = ttl - current_time;
      logger.info("Time diff  is " + time_diff);

      if(time_diff < 0){
         logger.error("Token is expired");
         throw new UnauthorizedError("Token is Expired");
      }

      User user = userRepository.findByUserName(email);
      if(user == null){
         logger.error("User not found " + email);
         throw new ResourceNotFoundException("User not found " + email);
      }
      if(user.getIs_valid() == false){
         user.setIs_valid(true);
         logger.info("User email is verified");
      }
      userRepository.save(user);
   }
}
