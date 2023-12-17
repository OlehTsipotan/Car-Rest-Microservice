package com.myapi.authserver.model;

public record AuthenticationTokenResponse(String access_token, String token_type, String expires_in, String scope) {
}
