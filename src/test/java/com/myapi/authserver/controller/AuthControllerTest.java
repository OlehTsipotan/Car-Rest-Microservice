package com.myapi.authserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapi.authserver.config.WebTestConfig;
import com.myapi.authserver.model.AuthenticationTokenResponse;
import com.myapi.authserver.model.UserCredentials;
import com.myapi.authserver.service.Auth0Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({AuthController.class})
@WithMockUser
@Import(WebTestConfig.class)
@ActiveProfiles(value = "test")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Auth0Service auth0Service;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void getToken_whenUserCredentialsAreValid_success() throws Exception {
        UserCredentials userCredentials = new UserCredentials("username", "password");

        AuthenticationTokenResponse authenticationTokenResponse = mock(AuthenticationTokenResponse.class);

        when(auth0Service.getToken(userCredentials)).thenReturn(authenticationTokenResponse);

        mockMvc.perform(post("/token").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCredentials)))
                .andExpect(status().isOk()).andExpect(content().string(objectMapper.writeValueAsString(authenticationTokenResponse)));

        verify(auth0Service).getToken(userCredentials);
        verifyNoMoreInteractions(auth0Service);
    }

    @Test
    public void getToken_whenUserCredentialsAreInvalid_statusIsBadRequest() throws Exception {
        UserCredentials userCredentials = new UserCredentials("username", "password");

        when(auth0Service.getToken(userCredentials)).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(post("/token").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCredentials)))
                .andExpect(status().isBadRequest());

        verify(auth0Service).getToken(userCredentials);
        verifyNoMoreInteractions(auth0Service);
    }


}
