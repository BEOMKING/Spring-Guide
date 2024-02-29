package com.spring.guide.retry.application;

import com.spring.guide.retry.domain.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class CollectAuditLogTemplateAnnotationService implements CollectAuditLogService {
    private final RetryTemplate retryTemplate;
    private final RestTemplate restTemplate;
    private int tokenRequestCount = 0;
    private int subscribeRequestCount = 0;
    private int auditLogRequestCount = 0;

    public CollectAuditLogTemplateAnnotationService(final RetryTemplate retryTemplate, final RestTemplate restTemplate) {
        this.retryTemplate = retryTemplate;
        this.restTemplate = restTemplate;
    }

    public Result retry() {
        return retryTemplate.execute(
                context -> {
                    if (tokenRequestCount % 2 == 0) {
                        tokenRequestCount++;
                        throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
                    }

                    if (subscribeRequestCount % 2 == 0) {
                        subscribeRequestCount++;
                        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
                    }

                    if (auditLogRequestCount % 2 == 0) {
                        auditLogRequestCount++;
                        throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
                    }

                    return Result.ok(context.getRetryCount(), restTemplate.getForObject("http://localhost:8080/audit", String.class));
                },
                context -> {
                    log.info("recover");
                    return Result.fail(context.getRetryCount(), context.getLastThrowable().getMessage());
                });
    }

}
