package com.example.dynamodbtest.security.principal;

import com.example.dynamodbtest.user.enumerate.State;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.dynamodbtest.user.dto.User;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 여기에 사용자 권한 설정 로직을 추가합니다.
        // 예를 들어, 사용자 역할(role)을 단일 문자열로 처리하려면 다음과 같이 할 수 있습니다.
        return Collections.singletonList((GrantedAuthority) () -> user.getRole().name());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // 필요에 따라 변경할 수 있습니다.
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // 필요에 따라 변경할 수 있습니다.
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // 필요에 따라 변경할 수 있습니다.
    }

    @Override
    public boolean isEnabled() {
        return user.getState() == State.ACTIVE;  // User의 상태에 따라 활성화 여부를 결정합니다.
    }
}
