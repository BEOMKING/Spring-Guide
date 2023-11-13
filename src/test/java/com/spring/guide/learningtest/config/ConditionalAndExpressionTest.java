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
    @DisplayName("Conditional 조건을 충족하면 빈이 생성된다.")
    void conditionalConfigBeanIsCreated() {
        contextRunner.withPropertyValues("product.premium.purchase=true")
                .run(context -> {
                    assertThat(context).hasSingleBean(ConditionalConfig.class);
                    assertThat(context.getBean(ConditionalConfig.class)).isNotNull();
                });
    }

    @Test
    @DisplayName("Conditional 조건을 충족하지 않으면 빈이 생성되지 않는다.")
    void conditionalConfigBeanIsNotCreated() {
        contextRunner.run(context -> assertThat(context).doesNotHaveBean(ConditionalConfig.class));
    }

    @Test
    @DisplayName("Expression 데이터를 세팅하지 않아도 기본 값이 있으면 기본 값을 반환한다.")
    void expressionActivateConfigWithDefaultValue() {
        contextRunner.withPropertyValues("product.premium.purchase=true")
                .run(context -> assertThat(context.getBean(ConditionalConfig.class).getHasDefaultValue()).isEqualTo("Basic"));
    }

    @Test
    @DisplayName("Expression 데이터를 세팅하지 않고 기본 값이 없으면 에러가 발생한다.")
    void expressionActivateConfigWithoutDefaultValue() {
        contextRunner.withPropertyValues("product.premium.purchase=true")
                .run(context -> assertThat(context.getBean(ConditionalConfig.class).getNoDefaultValue()).isEqualTo("${product.premium.grade}"));

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