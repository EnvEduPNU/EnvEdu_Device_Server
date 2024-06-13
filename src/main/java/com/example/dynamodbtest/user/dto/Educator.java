package com.example.dynamodbtest.user.dto;


import com.example.dynamodbtest.user.enumerate.Gender;
import com.example.dynamodbtest.user.enumerate.IsAuthorized;
import com.example.dynamodbtest.user.enumerate.Role;
import com.example.dynamodbtest.user.enumerate.State;
import lombok.*;

import java.sql.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import reactor.core.publisher.Mono;



@Table("educator")
@Getter
@Setter
@ToString
public class Educator extends User {

    @Builder(builderMethodName = "educatorBuilder")
    public Educator(String username, String password, String email, Date birthday, Role role, Gender gender, State state, String nickname, IsAuthorized isAuthorized)
    {
        super(username, password, email, birthday, role, gender, state, nickname);
        this.isAuthorized = isAuthorized;
    }
//
//    @Id
//    private Long idEducator;

//    @Column("educator_students")
//    private List<Student_Educator> educator_students;

    @Column("is_authorized")
    private IsAuthorized isAuthorized;

}

