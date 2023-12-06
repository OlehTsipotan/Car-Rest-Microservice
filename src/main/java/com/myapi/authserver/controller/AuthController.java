package com.myapi.authserver.controller;

import com.myapi.authserver.model.AuthenticationTokenResponse;
import com.myapi.authserver.model.UserCredentials;
import com.myapi.authserver.service.Auth0Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final Auth0Service auth0Service;

    @PostMapping("/token")
    public AuthenticationTokenResponse getToken(@RequestBody UserCredentials userCredentials) {
        return auth0Service.getToken(userCredentials);
    }
}
