package com.neu.cloudapp.security;

import com.neu.cloudapp.dao.UserCredentials;
import com.neu.cloudapp.exception.UnauthorizedError;
import org.springframework.stereotype.Component;
import java.util.Base64;

@Component
public class AuthenticationProvider {
    public static UserCredentials fetchCredentialsFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Basic")) {
            //Extract credentials
            String encodedUsernamePassword = authHeader.substring("Basic ".length()).trim();
            byte[] decodedBytes = Base64.getDecoder().decode(encodedUsernamePassword);
            String decodedString = new String(decodedBytes);
            String userEmail=decodedString.substring(0,decodedString.indexOf(":")).trim();
            String userPassword=decodedString.substring(decodedString.indexOf(":") + 1).trim();
            return new UserCredentials(userEmail, userPassword);
        } else {
            //Handle what happens if that isn't the case
            throw new UnauthorizedError("The authorization header is either empty or isn't Basic.");
        }
    }
}
