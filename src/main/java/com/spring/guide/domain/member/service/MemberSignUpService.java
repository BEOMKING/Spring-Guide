package com.spring.guide.domain.member.service;

import com.spring.guide.domain.member.dao.MemberRepository;
import com.spring.guide.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberSignUpService {
    private final MemberRepository memberRepository;

    public Member signUp(final Member member) {
        return memberRepository.signUp(member);
    }
}
