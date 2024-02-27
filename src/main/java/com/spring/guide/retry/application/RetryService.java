package com.spring.guide.retry.application;

import com.spring.guide.retry.domain.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Component
public class RetryService {
    private final RetryTemplate retryTemplate;
    private List<String> tokens = List.of("token0", "token1", "token2");
    private List<Boolean> subscribes = List.of(false, true, true);
    private List<Boolean> serverErrors = List.of(false, true, true);
    private Random random = new Random();

    public RetryService(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
    }

    private int count = 0;

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000), include = {Exception.class}, recover = "recover")
    public void retry1() {
        log.info("retry start count {}", count);
        if (count < 3) {
            count++;
            log.info("retry count {}", count);
            throw new RuntimeException("retry error");
        }
        log.info("retry end");
    }

    @Recover
    public void recover(Exception e) {
        log.info("recover");
    }

    public Result retry2() {
        log.info("retry2 start");
        return retryTemplate.execute(
                context -> {
                    final int retryCount = context.getRetryCount();
                    log.info("retry count {}, error {}", retryCount, Optional.ofNullable(context.getLastThrowable().getMessage()).orElseGet(() -> "null"));
                    return new Result(retryCount, true, result(), null);
                },
                context -> {
                    final int retryCount = context.getRetryCount();
                    log.info("retry count {}, error {}", retryCount, context.getLastThrowable().getMessage());
                    return new Result(retryCount, false, null, context.getLastThrowable().getMessage());
                });
    }

    public Result.SearchResult result() {
        final String token = tokens.get(random.nextInt(3));
        final boolean subscribe = subscribes.get(random.nextInt(3));
        final boolean serverError = serverErrors.get(random.nextInt(3));

        if (token.equals("token0")) {
            log.info("token is null");
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }

        if (!subscribe) {
            log.info("subscribe is false");
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }

        if (serverError) {
            log.info("server error");
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new Result.SearchResult(token, subscribe);
    }
}
