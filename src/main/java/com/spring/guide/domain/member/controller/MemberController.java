package com.spring.guide.domain.member.controller;

import com.spring.guide.domain.member.domain.Member;
import com.spring.guide.domain.member.dto.MemberCreateRequest;
import com.spring.guide.domain.member.dto.MemberResponse;
import com.spring.guide.domain.member.service.MemberSignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public Integer getMember(@RequestParam("age") final Integer age, @RequestParam("price") final Integer price) {
        return age + price;
    }
}
