package com.spring.guide.learningtest.config;

import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(ConditionalTrueTest.ConditionalConfig.class)
@TestPropertySource(properties = "product.premium.purchase=true")
class ConditionalTrueTest {

    @Autowired
    private ConditionalConfig conditionalConfig;

    @Test
    @DisplayName("Conditional 조건을 충족하면 빈이 생성된다.")
    void conditionalConfigBeanIsCreated() {
        assertThat(conditionalConfig).isNotNull();
    }

    @Test
    @DisplayName("Expression 데이터를 세팅하지 않아도 기본 값이 있으면 기본 값을 반환한다.")
    void expressionActivateConfigWithDefaultValue() {
        assertThat(conditionalConfig.getHasDefaultValue()).isEqualTo("Basic");
    }

    @TestConfiguration
    @ConditionalOnProperty(value = "product.premium.purchase", havingValue = "true")
    @Getter
    public static class ConditionalConfig {

        @Value("${product.premium.grade}")
        private String noDefaultValue;

        @Value("${product.premium.grade:Basic}")
        private String hasDefaultValue;

    }
}