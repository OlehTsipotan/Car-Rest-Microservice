package com.myapi.authserver.config;

import com.myapi.authserver.client.AuthClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.xml.sax.ErrorHandler;

@Configuration
public class WebConfig {

    @Value("${auth0.base-url}")
    private String baseUrl;

    @Bean
    RestClient restClient(RestClient.Builder builder) {
        return builder.baseUrl(baseUrl).build();
    }

    @Bean
    HttpServiceProxyFactory httpServiceProxyFactory(RestClient restClient) {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
    }

    @Bean
    AuthClient authClient(HttpServiceProxyFactory httpServiceProxyFactory) {
        return httpServiceProxyFactory.createClient(AuthClient.class);
    }
}
