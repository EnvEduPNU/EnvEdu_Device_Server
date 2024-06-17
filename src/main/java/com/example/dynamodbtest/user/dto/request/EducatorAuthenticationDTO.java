package com.example.dynamodbtest.user.dto.request;

import com.example.dynamodbtest.user.enumerate.IsAuthorized;
import lombok.Getter;

@Getter
public class EducatorAuthenticationDTO {
    private Long id;
    private IsAuthorized isAuthorized;
}
