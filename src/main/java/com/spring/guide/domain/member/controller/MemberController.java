package com.spring.guide.domain.member.controller;

import com.spring.guide.domain.member.domain.Member;
import com.spring.guide.domain.member.dto.MemberCreateRequest;
import com.spring.guide.domain.member.dto.MemberResponse;
import com.spring.guide.domain.member.service.MemberSignUpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberSignUpService memberSignUpService;

    @PostMapping
    public MemberResponse signUp(@Validated @RequestBody final MemberCreateRequest dto) {
        final Member member = dto.toEntity();
        final Member savedMember = memberSignUpService.signUp(member);
        return MemberResponse.of(savedMember);
    }
}
