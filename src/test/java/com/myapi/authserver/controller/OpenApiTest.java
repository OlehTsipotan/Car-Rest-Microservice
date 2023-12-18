package com.myapi.authserver.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(properties = {
        "auth0.client.id=id_from_env",
        "auth0.client.secret=secret_from_env",})
@AutoConfigureMockMvc
public class OpenApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testOpenApiDocs() throws Exception {
        mockMvc.perform(get("/docs")).andExpect(status().isOk());
    }

    @Test
    void testOpenApiHome() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html")).andExpect(status().isOk());
    }

}
