package com.myapi.authserver.controller;

import com.myapi.authserver.client.AuthClient;
import com.myapi.authserver.converter.PasswordAuthenticationRequestBodyFromUserCredentialsConverter;
import com.myapi.authserver.model.AuthenticationTokenResponse;
import com.myapi.authserver.model.PasswordAuthenticationRequestBody;
import com.myapi.authserver.model.UserCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthClient authClient;

    private final PasswordAuthenticationRequestBodyFromUserCredentialsConverter
            passwordAuthenticationRequestBodyFromUserCredentialsConverter;

    @PostMapping("/token")
    public AuthenticationTokenResponse getToken(@RequestBody UserCredentials userAuthenticationCredentials) {
        log.info("AuthController.getToken() userAuthenticationCredentials: {}", userAuthenticationCredentials);
        PasswordAuthenticationRequestBody passwordAuthenticationRequestBody =
                passwordAuthenticationRequestBodyFromUserCredentialsConverter.convert(userAuthenticationCredentials);
        log.info(passwordAuthenticationRequestBody.toString());
        return authClient.getToken(passwordAuthenticationRequestBody);
    }
}
