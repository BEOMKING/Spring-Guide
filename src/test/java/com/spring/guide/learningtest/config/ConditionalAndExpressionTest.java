package com.spring.guide.learningtest.config;

import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.annotation.UserConfigurations;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(ConditionalAndExpressionTest.ConditionalConfig.class)
class ConditionalAndExpressionTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(UserConfigurations.of(ConditionalConfig.class));

    @Test
    @DisplayName("Expression 값이 존재하면 ConditionalConfig 빈이 생성된다.")
    void conditionalConfigBeanIsCreated() {
        contextRunner.withPropertyValues("product.premium.purchase=true")
                .run(context -> {
                    assertThat(context).hasSingleBean(ConditionalConfig.class);
                    assertThat(context.getBean(ConditionalConfig.class)).isNotNull();
                });
    }

    @Test
    @DisplayName("Expression 값을 누락해도 기본 값 설정이 있으면 기본 값을 반환한다.")
    void expressionActivateConfigWithDefaultValue() {
        contextRunner.withPropertyValues("product.premium.purchase=true")
                .run(context -> assertThat(context.getBean(ConditionalConfig.class).getHasDefaultValue()).isEqualTo("Basic"));
    }

    @Test
    @DisplayName("Expression 값을 누락하고 기본 값이 없으면 에러를 발생시킨다.")
    void expressionActivateConfigWithoutDefaultValue() {
        contextRunner.withPropertyValues("product.premium.purchase=true")
                .run(context -> assertThat(context.getBean(ConditionalConfig.class).getNoDefaultValue()).isEqualTo("${product.premium.grade}"));

    }

    @Test
    @DisplayName("Expression 값이 누락되면 ConditionalConfig 빈이 생성되지 않는다.")
    void conditionalConfigBeanIsNotCreated() {
        contextRunner.run(context -> assertThat(context).doesNotHaveBean(ConditionalConfig.class));
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