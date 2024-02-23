package com.ll.demo.member.repository;

import com.ll.demo.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);


    Member findByNickname(String nickname);


    Optional<Member> findByRefreshToken(String refreshToken);

}
