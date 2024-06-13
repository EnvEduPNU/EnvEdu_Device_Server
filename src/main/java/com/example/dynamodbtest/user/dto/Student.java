package com.example.dynamodbtest.user.dto;

import com.example.dynamodbtest.user.enumerate.Gender;
import com.example.dynamodbtest.user.enumerate.Role;
import com.example.dynamodbtest.user.enumerate.State;
import lombok.Builder;
import java.sql.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Getter;
import lombok.Setter;

@Table("students")
@Getter
@Setter
public class Student extends User {

    @Builder(builderMethodName = "studentBuilder")
    public Student(String username, String password, String email, Date birthday, Role role, Gender gender, State state, String nickname) {
        super(username, password, email, birthday, role, gender, state, nickname);
    }

//    @Id
//    private Long idStudent;

//    @Column("student_educators")
//    private List<Student_Educator> student_educators;

}

