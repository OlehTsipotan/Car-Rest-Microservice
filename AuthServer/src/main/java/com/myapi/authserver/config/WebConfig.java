package com.myapi.authserver.config;

import com.myapi.authserver.client.Auth0Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

@Configuration
public class WebConfig {

    @Value("${auth0.base-url}")
    private String baseUrl;

    @Value("${client.connection-timeout}")
    private int connectTimeout;

    @Value("${client.read-timeout}")
    private int readTimeout;

    @Bean
    RestClient restClient(SimpleClientHttpRequestFactory simpleClientHttpRequestFactory) {
        return RestClient.builder()
                .requestFactory(simpleClientHttpRequestFactory)
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    SimpleClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(connectTimeout));
        factory.setReadTimeout(Duration.ofSeconds(readTimeout));
        return factory;
    }

    @Bean
    HttpServiceProxyFactory httpServiceProxyFactory(RestClient restClient) {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
    }

    @Bean
    Auth0Client authClient(HttpServiceProxyFactory httpServiceProxyFactory) {
        return httpServiceProxyFactory.createClient(Auth0Client.class);
    }
}
