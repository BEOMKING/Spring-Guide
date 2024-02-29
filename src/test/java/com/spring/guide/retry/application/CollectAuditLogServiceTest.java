package com.spring.guide.retry.application;

import com.spring.guide.retry.domain.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CollectAuditLogServiceTest {
    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private CollectAuditLogService collectAuditLogService;

    @Test
    @DisplayName("예외 발생시 재처리가 되고 성공을 반환한다.")
    void test() {
        // given
        given(restTemplate.getForObject("http://localhost:8080/audit", String.class)).willReturn("success");

        // when
        final Result result = collectAuditLogService.retry();

        // then
        assertThat(result.getRetryCount()).isEqualTo(3);
        assertThat(result.getSuccess()).isTrue();
        assertThat(result.getDetail()).isEqualTo("success");
    }

    @Test
    @DisplayName("예외 발생시 재처리가 되고 실패를 반환한다.")
    void test2() {
        // given
        given(restTemplate.getForObject("http://localhost:8080/audit", String.class)).willThrow(new RuntimeException("error"));

        // when
        final Result result = collectAuditLogService.retry();

        // then
        assertThat(result.getRetryCount()).isEqualTo(4);
        assertThat(result.getSuccess()).isFalse();
        assertThat(result.getDetail()).isEqualTo("error");
    }
}