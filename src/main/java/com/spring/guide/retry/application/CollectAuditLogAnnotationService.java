package com.spring.guide.retry.application;

import com.spring.guide.retry.domain.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class CollectAuditLogAnnotationService implements CollectAuditLogService {
    private final RestTemplate restTemplate;
    private int tokenRequestCount = 0;
    private int subscribeRequestCount = 0;
    private int auditLogRequestCount = 0;

    public CollectAuditLogAnnotationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retryable(maxAttempts = 4, backoff = @Backoff(delay = 1000), recover = "recover", listeners = "collectAuditLogListener")
    public Result retry() {
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

        return Result.ok(RetrySynchronizationManager.getContext().getRetryCount(), restTemplate.getForObject("http://localhost:8080/audit", String.class));
    }

    @Recover
    public Result recover(Exception e) {
        log.info("recover");
        return Result.fail(RetrySynchronizationManager.getContext().getRetryCount(), e.getMessage());
    }
}
