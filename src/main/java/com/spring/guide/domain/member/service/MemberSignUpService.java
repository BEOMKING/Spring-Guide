package com.spring.guide.domain.member.service;

import com.spring.guide.domain.member.dao.MemberRepository;
import com.spring.guide.domain.member.domain.Member;
import com.spring.guide.domain.member.exception.ResourceConflictException;
import com.spring.guide.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberSignUpService {
    private final MemberRepository memberRepository;

    public Member signUp(final Member member) {
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new ResourceConflictException(member.getEmail(), ErrorCode.EMAIL_DUPLICATION);
        }

        return memberRepository.signUp(member);
    }
}
