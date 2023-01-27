package com.neu.cloudapp.validator;

import com.neu.cloudapp.entity.User;
import com.neu.cloudapp.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserDetailsValidator {
    private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    // static Pattern object, since pattern is fixed
    private static Pattern pattern;

    // non-static Matcher object because it's created from the input String
    private Matcher matcher;

    public void checkUserData(String firstName, String lastName){
        if(firstName == null || firstName.length() == 0 || firstName.isBlank()){
            throw new BadRequestException("Empty first name, please enter valid first name" , "firstName");
        }
        if(lastName == null || lastName.length() == 0 || lastName.isBlank()){
            throw new BadRequestException("Empty last name, please enter valid last name" , "lastName");
        }
    }
    public void checkEmail(String email){
        pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        if(!pattern.matcher(email).matches()){
            throw new BadRequestException("Invalid user name, please enter valid user name " , "email");
        }
    }
    public void checkPassword(String password){
        if(password.isEmpty() || password.isBlank() || password.length() == 0){
            throw new BadRequestException("Empty Password, please enter valid password", "password");
        }
    }
    public void checkOtherFields(User user){
        if(user.getUser_name() != null && !user.getUser_name().isBlank()) {
            throw new BadRequestException("User Name can't be updated", user.getUser_name());
        }
        if(user.getId() != null ){
            throw new BadRequestException("Id can't be updated", String.valueOf(user.getId()));
        }
        if(user.getAccount_created() != null && !user.getAccount_created().isBlank()) {
            throw new BadRequestException("account created date can't be changed", user.getAccount_created());
        }
        if(user.getAccount_updated() != null && !user.getAccount_updated().isBlank()) {
            throw new BadRequestException("account updated date can't be changed", user.getAccount_updated());
        }
    }

    public String generateToken(){
        return UUID.randomUUID().toString();
    }
}
