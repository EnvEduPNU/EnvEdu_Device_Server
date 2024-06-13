package com.example.dynamodbtest.user.dto.request;

import lombok.Data;

@Data
public class LoginDTO {
    private String username;
    private String password;
}
