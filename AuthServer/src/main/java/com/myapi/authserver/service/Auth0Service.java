package com.myapi.authserver.service;

import com.myapi.authserver.client.Auth0Client;
import com.myapi.authserver.converter.PasswordAuthenticationRequestBodyFromUserCredentialsConverter;
import com.myapi.authserver.exception.RestClientException;
import com.myapi.authserver.model.AuthenticationTokenResponse;
import com.myapi.authserver.model.PasswordAuthenticationRequestBody;
import com.myapi.authserver.model.UserCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
@RequiredArgsConstructor
public class Auth0Service {

    private final Auth0Client auth0Client;

    private final PasswordAuthenticationRequestBodyFromUserCredentialsConverter
            passwordAuthenticationRequestBodyFromUserCredentialsConverter;

    public AuthenticationTokenResponse getToken(UserCredentials userCredentials) {
        PasswordAuthenticationRequestBody passwordAuthenticationRequestBody =
                passwordAuthenticationRequestBodyFromUserCredentialsConverter.convert(userCredentials);


        ResponseEntity<AuthenticationTokenResponse> responseEntity;
        try {
            responseEntity = auth0Client.getToken(passwordAuthenticationRequestBody);
        } catch (HttpClientErrorException e) {
            throw new RestClientException("Error while getting token from Auth0", e.getMessage(), e.getStatusCode());
        }

        return responseEntity.getBody();
    }
}
