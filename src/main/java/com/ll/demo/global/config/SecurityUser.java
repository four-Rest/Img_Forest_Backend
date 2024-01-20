package com.ll.demo.global.config;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class SecurityUser extends User {

    @Getter
    private long id;

    // 생성자: id, username, password, 권한을 받아서 User클래스 생성자 호출
    public SecurityUser(long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }


    // 생성자: username, password, 활성 여부, 계정 만료여부, 자격증명만료여부, 계정 잠금 여부, 권한을 받아서 User 클래스의 생성자 호출
    public SecurityUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }

    public Authentication genAuthentication() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                this,  // 사용자 정보를 나타내는 객체
                this.getPassword(),  // 패스워드
                this.getAuthorities()  // 사용자 권한
        );
        return auth;
    }
}
