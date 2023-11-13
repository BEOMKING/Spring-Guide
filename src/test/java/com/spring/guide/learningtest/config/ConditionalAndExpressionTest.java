package com.spring.guide.learningtest.config;

import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.*;

@SpringJUnitConfig(ConditionalAndExpressionTest.ConditionalConfig.class)
@TestPropertySource(properties = {"product.premium.purchase=true"})
class ConditionalAndExpressionTest {

    @Autowired
    private ConditionalConfig conditionalConfig;

    @Nested
    @DisplayName("ConditionalConfig가 활성화되면")
    class ActivateConditionalConfigTest {
        @Test
        @DisplayName("ConditionalConfig 빈이 생성된다.")
        void conditionalConfigBeanIsCreated() {
            assertThat(conditionalConfig).isNotNull();
        }
        @Test
        @DisplayName("Property에 키값을 누락했을 시 Expression의 기본 값이 있으면 기본 값을 반환한다.")
        void expressionActivateConfigWithDefaultValue() {
            assertThat(conditionalConfig.getHasDefaultValue()).isEqualTo("Basic");
        }

        @Test
        @DisplayName("Property에 키값을 누락했을 시 Expression의 기본 값이 없으면 에러를 발생시킨다.")
        void expressionActivateConfigWithoutDefaultValue() {
            assertThat(conditionalConfig.getNoDefaultValue()).isEqualTo("${product.premium.grade}");
        }

    }

    @Nested
    @DisplayName("ConditionalConfig가 비활성화되면")
    @TestPropertySource(properties = {"product.premium.purchase=false"})
    class DeactivateConditionalConfigTest {
        @Autowired
        private ConditionalConfig conditionalConfig;

        @Test
        @DisplayName("ConditionalConfig 빈이 생성되지 않는다.")
        void conditionalConfigBeanIsNotCreated() {
            assertThat(conditionalConfig).isNull();
        }

        @Test
        @DisplayName("Property에 키값이 누락되어도 에러를 발생시키지 않는다.")
        void expressionDeactivateConfig() {
            assertThatNoException().isThrownBy(() -> conditionalConfig.getNoDefaultValue());
        }

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