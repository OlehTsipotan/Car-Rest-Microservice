package com.myapi.authserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "auth0.client.id=id_from_env",
        "auth0.client.secret=secret_from_env",})
public class AuthserverApplicationTest {

    @Test
    void contextLoads() {
    }
}
