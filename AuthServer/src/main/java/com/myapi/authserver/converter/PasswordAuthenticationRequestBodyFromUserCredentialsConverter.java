package com.myapi.authserver.converter;

import com.myapi.authserver.model.PasswordAuthenticationRequestBody;
import com.myapi.authserver.model.UserCredentials;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PasswordAuthenticationRequestBodyFromUserCredentialsConverter {

    @Value("password")
    private String grantType;

    @Value("${auth0.audience}")
    private String audience;

    @Value("${auth0.client.id}")
    private String clientId;

    @Value("${auth0.client.secret}")
    private String clientSecret;

    public PasswordAuthenticationRequestBody convert(@NonNull UserCredentials userCredentials) {
        return new PasswordAuthenticationRequestBody(grantType, userCredentials.username(), userCredentials.password(),
                audience, clientId, clientSecret);
    }
}
