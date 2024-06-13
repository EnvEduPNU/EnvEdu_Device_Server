package com.example.dynamodbtest.user.dto.response;


import com.example.dynamodbtest.user.dto.User;
import com.example.dynamodbtest.user.enumerate.Gender;
import com.example.dynamodbtest.user.enumerate.Role;
import com.example.dynamodbtest.user.enumerate.State;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
public class UserDto {
    private final Long id;
    private final String username;
    private final String email;
    private final Date birthday;
    private final Role role;
    private final Gender gender;
    private final State state;
    private final String nickname;
    private final Timestamp updatedTime;


    public UserDto(User user) {
        id = user.getIdUser();
        username = user.getUsername();
        email = user.getEmail();
        birthday = user.getBirthday();
        role = user.getRole();
        gender = user.getGender();
        state = user.getState();
        updatedTime = Timestamp.valueOf(user.getUpdatedTime());
        nickname = user.getNickname();
    }
}
