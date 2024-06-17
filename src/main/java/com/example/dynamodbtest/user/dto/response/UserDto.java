package com.example.dynamodbtest.user.dto.response;

import com.example.dynamodbtest.user.dto.User;
import com.example.dynamodbtest.user.enumerate.Gender;
import com.example.dynamodbtest.user.enumerate.Role;
import com.example.dynamodbtest.user.enumerate.State;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class UserDto {
    private final Long id;
    private final String username;
    private final String email;
    private final LocalDate birthday; // 변경된 부분
    private final Role role;
    private final Gender gender;
    private final State state;
    private final String nickname;
    private final LocalDateTime updatedTime; // 변경된 부분

    public UserDto(User user) {
        id = user.getIdUser();
        username = user.getUsername();
        email = user.getEmail();
        birthday = user.getBirthday(); // 변경된 부분
        role = user.getRole();
        gender = user.getGender();
        state = user.getState();
        updatedTime = user.getUpdatedTime(); // 변경된 부분
        nickname = user.getNickname();
    }
}
