package com.myapi.authserver.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapi.authserver.model.AuthenticationTokenResponse;
import com.myapi.authserver.model.PasswordAuthenticationRequestBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestTemplateAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ExtendWith(SpringExtension.class)
public class Auth0ClientTest {

    private MockRestServiceServer mockServer;

    private Auth0Client auth0Client;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        this.mockServer = MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build();

        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(RestTemplateAdapter.create(restTemplate)).build();
        this.auth0Client = factory.createClient(Auth0Client.class);
    }

    @Test
    void getToken_success() throws JsonProcessingException {
        PasswordAuthenticationRequestBody passwordAuthenticationRequestBody =
                new PasswordAuthenticationRequestBody("grant_type", "username", "password", "audience", "client_id",
                        "client_secret");
        AuthenticationTokenResponse authenticationTokenResponse =
                new AuthenticationTokenResponse("access_token", "token_type", "10", "scope");

        String jsonResponse = objectMapper.writeValueAsString(authenticationTokenResponse);
        String jsonRequestBody = objectMapper.writeValueAsString(passwordAuthenticationRequestBody);

        this.mockServer.expect(requestTo("/oauth/token"))
                .andExpect(content().string(jsonRequestBody))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        assertEquals(authenticationTokenResponse, auth0Client.getToken(passwordAuthenticationRequestBody).getBody());

        this.mockServer.verify();
    }

}
