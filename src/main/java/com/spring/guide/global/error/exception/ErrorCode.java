package com.spring.guide.global.error.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE("C001", "Invalid Input Value"),
    INVALID_TYPE_VALUE("C002", "Invalid Type Value"),
    METHOD_NOT_ALLOWED("C002", "Method Not Allowed"),
    INTERNAL_SERVER_ERROR("C003", "Server Error"),

    // Member
    EMAIL_DUPLICATION("M001", "Email is Duplication"),
    INVALID_MEMBER("M002", "Invalid Member");

    private final String code;
    private final String message;

    ErrorCode(final String code, final String message) {
        this.code = code;
        this.message = message;
    }
}
