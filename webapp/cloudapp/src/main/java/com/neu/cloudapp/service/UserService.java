package com.neu.cloudapp.service;

import com.neu.cloudapp.config.MetricsClientBean;
import com.neu.cloudapp.dao.UserCredentials;
import com.neu.cloudapp.dao.UserSNSEvent;
import com.neu.cloudapp.entity.User;
import com.neu.cloudapp.exception.BadRequestException;
import com.neu.cloudapp.exception.ResourceNotFoundException;
import com.neu.cloudapp.exception.UnauthorizedError;
import com.neu.cloudapp.publisher.SNSPublisher;
import com.neu.cloudapp.repository.UserRepository;
import com.neu.cloudapp.security.AuthenticationProvider;
import com.neu.cloudapp.security.PasswordEncoder;
import com.neu.cloudapp.validator.UserDetailsValidator;
import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.util.UUID;

@Service
@Transactional
public class UserService {
   @Autowired
   UserRepository userRepository;

    @Autowired
    StatsDClient statsDClient;

    @Autowired
    DynamoDBService dynamoDBService;

    @Autowired
    UserDetailsValidator userDetailsValidator;

    @Autowired
    SNSPublisher snsPublisher;

   public UserService(UserRepository userRepository){
       this.userRepository = userRepository;
   }

   private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    public User createUser(User user) {
       if (checkIfUserExists(user.getUser_name())){
           logger.error("Email Already Exists {}", user.getUser_name());
           throw new BadRequestException("User already exists" , user.getUser_name());
       }
        user.setPassword(PasswordEncoder.encodePassword(user.getPassword()));
        user.setAccount_created(String.valueOf(new Timestamp(System.currentTimeMillis())));
        user.setAccount_updated(String.valueOf(new Timestamp(System.currentTimeMillis())));
        user.setIs_valid(false);
        logger.info("Creating User in Database with Email {}", user.getUser_name());
        userRepository.save(user);
        logger.info("Created User in Database {}", user.getUser_name());

        String token = userDetailsValidator.generateToken();
        logger.info("Generated Token " + token);
        dynamoDBService.saveInDynamoDB(user.getUser_name(),token);
        logger.info("Saved in Dynamo Db ");
        snsPublisher.publishMessage(new UserSNSEvent(user.getUser_name(), token, "VERIFY"));
        logger.info("Message published");
        return user;
    }

    public User getUser(String authHeader, UUID id){
        UserCredentials userCredentials = AuthenticationProvider.fetchCredentialsFromHeader(authHeader);
        logger.info("Getting User in Database with Email {}", userCredentials.getUserName());
        User user=userRepository.findByUserNameAndId(userCredentials.getUserName(), id);
        if(user == null){
            logger.error("User not found", userCredentials.getUserName());
            throw new ResourceNotFoundException("User not found " + userCredentials.getUserName() );
        }
        if(user.getIs_valid() == false){
            logger.error("In get user details, user is not verified");
            throw new UnauthorizedError("User is not verified");
        }
        PasswordEncoder.checkPassword(userCredentials.getPassword(), user.getPassword());
     return user;
    }

    public User updateUser(String authHeader, User userDetails, UUID id){
        UserCredentials userCredentials = AuthenticationProvider.fetchCredentialsFromHeader(authHeader);
        User user = userRepository.findByUserNameAndId(userCredentials.getUserName(), id);
        if(user == null){
            logger.error("User not found", userCredentials.getUserName());
            throw new ResourceNotFoundException("User not found " + userCredentials.getUserName());
        }
        if(user.getIs_valid() == false){
            logger.error("In update user details, user is not verified");
            throw new UnauthorizedError("User is not verified");
        }
        PasswordEncoder.checkPassword(userCredentials.getPassword(), user.getPassword());
        user.setFirst_name(userDetails.getFirst_name());
        user.setLast_name(userDetails.getLast_name());
        user.setPassword(PasswordEncoder.encodePassword(userDetails.getPassword()));
        user.setAccount_updated(String.valueOf(new Timestamp(System.currentTimeMillis())));
        logger.info("Updating User in Database with Email {}", userCredentials.getUserName());
        final User updatedUser = userRepository.save(user);
        logger.info("Updated User in Database with Email {}", userCredentials.getUserName());
        return updatedUser;
   }
    public boolean checkIfUserExists(String username){
       if (userRepository.findByUserName(username) != null){
           return true;
       }else{
           return false;
       }
    }
 }

