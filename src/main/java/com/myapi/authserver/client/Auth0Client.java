package com.myapi.authserver.client;

import com.myapi.authserver.model.AuthenticationTokenResponse;
import com.myapi.authserver.model.PasswordAuthenticationRequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface Auth0Client {

    @PostExchange(value = "/oauth/token")
    ResponseEntity<AuthenticationTokenResponse> getToken(
            @RequestBody PasswordAuthenticationRequestBody passwordAuthenticationRequestBody);


}
