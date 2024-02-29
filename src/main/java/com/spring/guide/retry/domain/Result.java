package com.spring.guide.retry.domain;

import lombok.Getter;

@Getter
public class Result {
    Integer retryCount;
    Boolean success;
    String detail;

    public Result() {
    }

    public Result(Integer retryCount, Boolean success, String detail) {
        this.retryCount = retryCount;
        this.success = success;
        this.detail = detail;
    }

    public static Result ok(final int retryCount, final String result) {
        return new Result(retryCount, true, result);
    }

    public static Result fail(final int retryCount, String message) {
        return new Result(retryCount, false, message);
    }

}
