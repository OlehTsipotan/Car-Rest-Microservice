package com.myapi.authserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Data
public class PasswordAuthenticationRequestBody {

    private String grant_type;

    private String username;

    private String password;

    private String audience;

    private String client_id;

    private String client_secret;

}
