package com.example.dynamodbtest.user.enumerate;


import com.example.dynamodbtest.user.dto.Educator;
import com.example.dynamodbtest.user.dto.Student;
import com.example.dynamodbtest.user.dto.User;
import com.example.dynamodbtest.user.dto.request.RegisterDTO;
import org.springframework.security.crypto.password.PasswordEncoder;

public enum Role {
    ROLE_STUDENT {
        @Override
        public User generateUserByRole(RegisterDTO registerDTO, PasswordEncoder passwordEncoder) {
            return Student.studentBuilder()
                    .username(registerDTO.getUsername())
                    .password(passwordEncoder.encode(registerDTO.getPassword()))
                    .email(registerDTO.getEmail())
                    .gender(registerDTO.getGender())
                    .role(registerDTO.getRole())
                    .state(State.ACTIVE)
                    .birthday(registerDTO.getBirthday().toLocalDate())
                    .nickname(registerDTO.getNickname())
                    .build();
        }
    }, ROLE_EDUCATOR {
        @Override
        public User generateUserByRole(RegisterDTO registerDTO, PasswordEncoder passwordEncoder) {
            return Educator.educatorBuilder()
                    .username(registerDTO.getUsername())
                    .password(passwordEncoder.encode(registerDTO.getPassword()))
                    .email(registerDTO.getEmail())
                    .gender(registerDTO.getGender())
                    .role(registerDTO.getRole())
                    .state(State.ACTIVE)
                    .isAuthorized(IsAuthorized.NO)
                    .birthday(registerDTO.getBirthday().toLocalDate())
                    .nickname(registerDTO.getNickname())
                    .build();
        }
    };

    public abstract User generateUserByRole(RegisterDTO registerDTO, PasswordEncoder passwordEncoder);
}
