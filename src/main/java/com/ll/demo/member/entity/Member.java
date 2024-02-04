package com.ll.demo.member.entity;


import com.ll.demo.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity {
    @Column(unique = true)
    private String username;

    private String password;
    private String email;
    private String nickname;
    private String refreshToken;


    // 사용자 권한처리
    @SuppressWarnings("JpaAttributeTypeInspection")
    public List<? extends GrantedAuthority> getAuthorities() {   // Security에서 사용자의 권한 정보를 얻을 때 호출
        return getAuthoritiesAsStrList()  // 문자열 형태의 권한 목록을 가져옴
                .stream()
                .map(SimpleGrantedAuthority::new)  // 각 권한 매핑
                .toList();
    }

    @SuppressWarnings("JpaAttributeTypeInspection")  // 사용자의 권한을 문자열 형태로 반환
    public List<String> getAuthoritiesAsStrList() {  // ROLE_MEMBER 문자열을 포함한 리스트 반환
        return List.of("ROLE_MEMBER");
    }
}