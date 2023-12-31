package com.spring.guide.domain.member.controller;

import com.spring.guide.ControllerTestSupport;
import com.spring.guide.domain.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends ControllerTestSupport {
    @Test
    @DisplayName("회원 가입한다.")
    void signUp() throws Exception {
        // given
        final Map<String, Object> member = Map.of(
                "email", "qjawlsqjacks@naver.com",
                "name", "BJP",
                "password", "12345678",
                "age", 28
        );

        when(memberSignUpService.signUp(any())).thenReturn(Member.builder()
                .id(1L)
                .email("qjawlsqjacks@naver.com")
                .name("BJP")
                .age(28)
                .build());

        // when & then
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("qjawlsqjacks@naver.com"))
                .andExpect(jsonPath("$.name").value("BJP"))
                .andExpect(jsonPath("$.age").value(28));
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