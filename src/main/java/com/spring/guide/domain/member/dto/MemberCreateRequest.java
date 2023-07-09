package com.spring.guide.domain.member.dto;

import com.spring.guide.domain.member.domain.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

@Getter
public class MemberCreateRequest {
    @Email
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 8, max = 20, message = "{Size.password}")
    private String password;

    @NotNull
    @Range(min = 20, max = 30)
    private Integer age;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .name(name)
                .password(password)
                .build();
    }
}
