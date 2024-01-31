package com.ll.demo.global.service;

import com.ll.demo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SystemService {
    private final MemberRepository memberRepository;

    public boolean testDataCreated() {
        return memberRepository.findByUsername("testUserForImgTest").isPresent();
    }

}
