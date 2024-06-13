package com.example.dynamodbtest.user.dto;

import com.example.dynamodbtest.user.enumerate.Gender;
import com.example.dynamodbtest.user.enumerate.Role;
import com.example.dynamodbtest.user.enumerate.State;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.sql.Date;
import java.time.LocalDateTime;

@Table("users")
@Getter
@Setter
public abstract class User {

    protected User(String username, String password, String email, Date birthday, Role role, Gender gender, State state, String nickname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthday = birthday;
        this.role = role;
        this.gender = gender;
        this.state = state;
        this.nickname = nickname;
    }

    @Id
    private Long idUser;

    @Column("username")
    private String username;

    @Column("password")
    private String password;

    @Column("email")
    private String email;

    @Column("birthday")
    private Date birthday;

    @Column("role")
    private Role role;

    @Column("gender")
    private Gender gender;

    @Column("state")
    private State state;

    @Column("nickname")
    private String nickname;


    @CreatedDate
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime updatedTime;


}
