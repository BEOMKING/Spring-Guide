package com.spring.guide.domain.member.controller;

import com.spring.guide.domain.member.domain.Member;
import com.spring.guide.domain.member.dto.MemberCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends IntegrationTestSupport {

    @Test
    @DisplayName("이미 존재하는 이메일로 회원가입을 요청하면 409 에러를 반환한다.")
    void returnExceptionIfAlreadyExistsEmail() throws Exception {
        // given
        final Member member = Member.builder()
                .email("qjawlsqjacks@naver.com")
                .name("BJP")
                .password("12345678")
                .age(20)
                .build();

        memberSignUpService.signUp(member);

        // when & then
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(member)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("숫자 타입의 ID에 문자열을 입력하면 400 에러를 반환한다.")
    void returnExceptionIfTypeMissMatch() throws Exception {
        // given
        final Map<String, Object> member = Map.of(
                "email", "qjawlsqjacks@naver.com",
                "name", "BJP",
                "password", "12345678",
                "age", "AGE"
        );

        // when & then
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(member)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이름에 공백을 입력하면 400 에러를 반환한다.")
    void returnExceptionIfEmptyName() throws Exception {
        // given
        final Member member = Member.builder()
                .email("qjawlsqjacks@naver.com")
                .name("")
                .password("12345678")
                .build();

        // when & then
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(member)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}