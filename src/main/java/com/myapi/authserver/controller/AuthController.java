package com.myapi.authserver.controller;

import com.myapi.authserver.model.AuthenticationTokenResponse;
import com.myapi.authserver.model.UserCredentials;
import com.myapi.authserver.service.Auth0Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final Auth0Service auth0Service;

    @Operation(summary = "Retrieve the Token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token retrieved successfully", content = {
                    @Content(mediaType = "application/json",
                             schema = @Schema(implementation = AuthenticationTokenResponse.class))}),
            @ApiResponse(responseCode = "502", description = "Token retrieving error ", content = @Content)})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/token")
    public AuthenticationTokenResponse getToken(@RequestBody UserCredentials userCredentials) {
        return auth0Service.getToken(userCredentials);
    }
}
