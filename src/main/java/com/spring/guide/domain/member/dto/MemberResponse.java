package com.spring.guide.domain.member.dto;

import com.spring.guide.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {
    private final Long id;
    private final String email;
    private final String name;

    public MemberResponse(final Long id, final String email, final String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public static MemberResponse of(final Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }
}
