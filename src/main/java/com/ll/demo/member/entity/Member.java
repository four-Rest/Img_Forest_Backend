package com.ll.demo.member.entity;


import com.ll.demo.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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

//    @SuppressWarnings("JpaAttributeTypeInspection")
//    public List<? extends GrantedAuthority> getAuthorities() {
//        return getAuthoritiesAsStrList()
//                .stream()
//                .map(SimpleGrantedAuthority::new)
//                .toList();
//    }
//
    @SuppressWarnings("JpaAttributeTypeInspection")
    public List<String> getAuthoritiesAsStrList() {
        return List.of("ROLE_MEMBER");
    }
}