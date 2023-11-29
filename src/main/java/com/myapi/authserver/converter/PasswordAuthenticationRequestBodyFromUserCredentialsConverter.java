package com.myapi.authserver.converter;

import com.myapi.authserver.model.PasswordAuthenticationRequestBody;
import com.myapi.authserver.model.UserCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PasswordAuthenticationRequestBodyFromUserCredentialsConverter {

    @Value("password")
    private String grant_type;

    @Value("${auth0.audience}")
    private String audience;

    @Value("${auth0.client.id}")
    private String client_id;

    @Value("${auth0.client.secret}")
    private String client_secret;

    public PasswordAuthenticationRequestBody convert(UserCredentials userCredentials) {
        return new PasswordAuthenticationRequestBody(grant_type, userCredentials.username(), userCredentials.password(),
                audience, client_id, client_secret);
    }
}
