package com.spring.guide.retry.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component("collectAuditLogListener")
public class CollectAuditLogListener implements RetryListener {

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
        log.info("open");
        return true;
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        log.info("close");
    }

    @Override
    public <T, E extends Throwable> void onSuccess(final RetryContext context, final RetryCallback<T, E> callback, final T result) {
        log.info("retry count: {} success", context.getRetryCount());
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        log.info("retry count: {} error {}", context.getRetryCount(), throwable.getMessage());
    }
}
