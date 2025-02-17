package com.example.dynamodbtest.user.dto;


import com.example.dynamodbtest.user.enumerate.Gender;
import com.example.dynamodbtest.user.enumerate.IsAuthorized;
import com.example.dynamodbtest.user.enumerate.Role;
import com.example.dynamodbtest.user.enumerate.State;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;


@Table("educator")
@Getter
@Setter
@ToString
public class Researcher extends User {

    @Builder(builderMethodName = "researcherBuilder")
    public Researcher(String username, String password, String email, LocalDate birthday, Role role, Gender gender, State state, String nickname, IsAuthorized isAuthorized)
    {
        super(username, password, email, birthday, role, gender, state, nickname);
        this.isAuthorized = isAuthorized;
    }

    @Column("is_authorized")
    private IsAuthorized isAuthorized;

}

