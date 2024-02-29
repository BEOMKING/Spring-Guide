package com.spring.guide.retry.infra;

import com.spring.guide.retry.application.CollectAuditLogListener;
import com.spring.guide.retry.application.CollectAuditLogTemplateAnnotationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Slf4j
@Configuration
@ConditionalOnBean(CollectAuditLogTemplateAnnotationService.class)
public class RetryTemplateConfig {

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
        fixedBackOffPolicy.setBackOffPeriod(1000L);
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(4);
        retryTemplate.registerListener(new CollectAuditLogListener());
        retryTemplate.setRetryPolicy(retryPolicy);
        return retryTemplate;
    }
}
