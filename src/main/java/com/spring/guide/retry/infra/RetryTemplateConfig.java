package com.spring.guide.retry.infra;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class RetryTemplateConfig {

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
        fixedBackOffPolicy.setBackOffPeriod(1000L);
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
//        SimpleRetryPolicy badRequestPolicy = new SimpleRetryPolicy(3, Collections.singletonMap(HttpClientErrorException.BadRequest.class, true));
//        SimpleRetryPolicy unauthorizedPolicy = new SimpleRetryPolicy(2, Collections.singletonMap(HttpClientErrorException.Unauthorized.class, true));
//        SimpleRetryPolicy exceptionPolicy = new SimpleRetryPolicy(4, Collections.singletonMap(HttpClientErrorException.class, true));
//        List<RetryPolicy> policies = List.of(exceptionPolicy, badRequestPolicy, unauthorizedPolicy);
//        ExceptionClassifierRetryPolicy retryPolicy = new ExceptionClassifierRetryPolicy();
//        Map<Class<? extends Throwable>, RetryPolicy> policyMap = new HashMap<>();
//        policyMap.put(Exception.class, exceptionPolicy);
//        policyMap.put(HttpClientErrorException.BadRequest.class, badRequestPolicy);
//        policyMap.put(HttpClientErrorException.Unauthorized.class, unauthorizedPolicy);
//        retryPolicy.setPolicyMap(policyMap);
//        CompositeRetryPolicy retryPolicy = new CompositeRetryPolicy();
//        retryPolicy.setPolicies(policies.toArray(new RetryPolicy[0]));
        retryTemplate.setRetryPolicy(retryPolicy);
        return retryTemplate;
    }
}
