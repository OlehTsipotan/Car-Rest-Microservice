package com.myapi.authserver.client;

import com.myapi.authserver.model.AuthenticationTokenResponse;
import com.myapi.authserver.model.PasswordAuthenticationRequestBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface AuthClient {

    @PostExchange(value = "/oauth/token")
    AuthenticationTokenResponse getToken(
            @RequestBody PasswordAuthenticationRequestBody passwordAuthenticationRequestBody);


}
