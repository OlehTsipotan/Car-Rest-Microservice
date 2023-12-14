package com.myapi.authserver.converter;

import com.myapi.authserver.model.PasswordAuthenticationRequestBody;
import com.myapi.authserver.model.UserCredentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PasswordAuthenticationRequestBodyFromUserCredentialsConverterTest {

    private PasswordAuthenticationRequestBodyFromUserCredentialsConverter
            passwordAuthenticationRequestBodyFromUserCredentialsConverter;

    @BeforeEach
    void setUp() {
        passwordAuthenticationRequestBodyFromUserCredentialsConverter =
                new PasswordAuthenticationRequestBodyFromUserCredentialsConverter();
    }

    @ParameterizedTest
    @NullSource
    void convert_whenUserCredentialsIsNull_throwIllegalArgumentException(UserCredentials nullUserCredentials) {
        assertThrows(IllegalArgumentException.class,
                () -> passwordAuthenticationRequestBodyFromUserCredentialsConverter.convert(nullUserCredentials));
    }

    @Test
    void convert_whenUserCredentialsFieldsAreNull_success() {
        UserCredentials userCredentials = new UserCredentials(null, null);
        PasswordAuthenticationRequestBody passwordAuthenticationRequestBody =
                new PasswordAuthenticationRequestBody(null, null, null, null, null, null);

        PasswordAuthenticationRequestBody passwordAuthenticationRequestBodyConverted =
                passwordAuthenticationRequestBodyFromUserCredentialsConverter.convert(userCredentials);

        assertEquals(passwordAuthenticationRequestBody.getGrant_type(),
                passwordAuthenticationRequestBodyConverted.getGrant_type());
        assertEquals(passwordAuthenticationRequestBody.getUsername(),
                passwordAuthenticationRequestBodyConverted.getUsername());
        assertEquals(passwordAuthenticationRequestBody.getPassword(),
                passwordAuthenticationRequestBodyConverted.getPassword());
        assertEquals(passwordAuthenticationRequestBody.getAudience(),
                passwordAuthenticationRequestBodyConverted.getAudience());
        assertEquals(passwordAuthenticationRequestBody.getClient_id(),
                passwordAuthenticationRequestBodyConverted.getClient_id());
        assertEquals(passwordAuthenticationRequestBody.getClient_secret(),
                passwordAuthenticationRequestBodyConverted.getClient_secret());
    }

    @Test
    void convert_whenUserCredentialsFieldsAreNotNull_success() {
        UserCredentials userCredentials = new UserCredentials("username", "password");
        PasswordAuthenticationRequestBody passwordAuthenticationRequestBody =
                new PasswordAuthenticationRequestBody(null, "username", "password", null, null, null);

        PasswordAuthenticationRequestBody passwordAuthenticationRequestBodyConverted =
                passwordAuthenticationRequestBodyFromUserCredentialsConverter.convert(userCredentials);

        assertEquals(passwordAuthenticationRequestBody.getGrant_type(),
                passwordAuthenticationRequestBodyConverted.getGrant_type());
        assertEquals(passwordAuthenticationRequestBody.getUsername(),
                passwordAuthenticationRequestBodyConverted.getUsername());
        assertEquals(passwordAuthenticationRequestBody.getPassword(),
                passwordAuthenticationRequestBodyConverted.getPassword());
        assertEquals(passwordAuthenticationRequestBody.getAudience(),
                passwordAuthenticationRequestBodyConverted.getAudience());
        assertEquals(passwordAuthenticationRequestBody.getClient_id(),
                passwordAuthenticationRequestBodyConverted.getClient_id());
        assertEquals(passwordAuthenticationRequestBody.getClient_secret(),
                passwordAuthenticationRequestBodyConverted.getClient_secret());
    }

}
