package com.spring.guide.domain.member.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Member {
    private Long id;
    private String email;
    private String name;
    private String password;

    public void setId(final Long id) {
        this.id = id;
    }
}
