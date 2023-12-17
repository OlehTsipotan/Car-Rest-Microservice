package com.myapi.authserver.service;

import com.myapi.authserver.client.Auth0Client;
import com.myapi.authserver.converter.PasswordAuthenticationRequestBodyFromUserCredentialsConverter;
import com.myapi.authserver.exception.RestClientException;
import com.myapi.authserver.model.AuthenticationTokenResponse;
import com.myapi.authserver.model.UserCredentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class Auth0ServiceTest {

    @Mock
    private Auth0Client auth0Client;

    @Mock
    private PasswordAuthenticationRequestBodyFromUserCredentialsConverter
            passwordAuthenticationRequestBodyFromUserCredentialsConverter;

    private Auth0Service auth0Service;

    @BeforeEach
    public void setUp() {
        auth0Service = new Auth0Service(auth0Client, passwordAuthenticationRequestBodyFromUserCredentialsConverter);
    }

    @ParameterizedTest
    @NullSource
    void getToken_whenUserCredentialsIsNull_throwIllegalArgumentException(UserCredentials nullUserCredentials) {
        when(passwordAuthenticationRequestBodyFromUserCredentialsConverter.convert(null)).thenThrow(
                IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> auth0Service.getToken(nullUserCredentials));
    }

    @Test
    void getToken_whenAuth0ClientThrowsHttpClientErrorException_throwRestClientException() {
        UserCredentials userCredentials = new UserCredentials("username", "password");

        when(passwordAuthenticationRequestBodyFromUserCredentialsConverter.convert(userCredentials)).thenReturn(null);
        when(auth0Client.getToken(null)).thenThrow(HttpClientErrorException.class);

        assertThrows(RestClientException.class, () -> auth0Service.getToken(userCredentials));
    }

    @Test
    void getToken_success() {
        UserCredentials userCredentials = new UserCredentials("username", "password");
        AuthenticationTokenResponse authenticationTokenResponse = mock(AuthenticationTokenResponse.class);

        when(passwordAuthenticationRequestBodyFromUserCredentialsConverter.convert(userCredentials)).thenReturn(null);
        when(auth0Client.getToken(null)).thenReturn(
                new ResponseEntity<>(authenticationTokenResponse, null, HttpStatusCode.valueOf(200)));

        assertEquals(authenticationTokenResponse, auth0Service.getToken(userCredentials));
    }


}
