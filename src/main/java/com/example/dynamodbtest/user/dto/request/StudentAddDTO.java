package com.example.dynamodbtest.user.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class StudentAddDTO {
    private List<String> studentUsernames;

    public StudentAddDTO(List<String> studentUsernames) {
        this.studentUsernames = studentUsernames;
    }

    @Override
    public String toString() {
        return studentUsernames.toString();
    }
}
