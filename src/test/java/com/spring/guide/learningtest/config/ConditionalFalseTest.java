
package com.spring.guide.learningtest.config;

import lombok.Getter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.annotation.UserConfigurations;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>ConditionalOnProperty 조건을 충족하지 않으므로 ConditionalConfig 빈이 생성되지 않는다.
 * <p>따라서 ConditionalConfig 빈을 주입받을 수 없기에 테스트를 실행조차 할 수 없다.
 */
@SpringJUnitConfig(ConditionalFalseTest.ConditionalConfig.class)
@TestPropertySource(properties = "product.premium.purchase=false")
@Disabled
class ConditionalFalseTest {

    @Autowired
    private ConditionalConfig conditionalConfig;

    @Test
    @DisplayName("Conditional 조건을 충족하지 않으면 빈이 생성되지 않는다.")
    void conditionalConfigBeanIsNotCreated() {
        assertThat(conditionalConfig).isNull();
    }

    @Test
    @DisplayName("Expression 데이터를 세팅하지 않고 기본 값이 없으면 에러가 발생한다.")
    void expressionActivateConfigWithoutDefaultValue() {
        assertThat(conditionalConfig.getNoDefaultValue()).isEqualTo("${product.premium.grade}");
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