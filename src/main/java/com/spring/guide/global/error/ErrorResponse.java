package com.spring.guide.global.error;

import com.spring.guide.global.error.exception.ErrorCode;
import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Getter
public class ErrorResponse {
    private String code;
    private String message;
    private List<CustomError> errors;

    public ErrorResponse() {
    }

    public ErrorResponse(String code) {
        this.code = code;
    }

    public ErrorResponse(String code, String message) {
        this(code);
        this.message = message;
        this.errors = new ArrayList<>();
    }

    public ErrorResponse(String code, String message, List<CustomError> errors) {
        this(code, message);
        this.errors = errors;
    }

    public static ErrorResponse of(final ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
    }

    public static ErrorResponse of(final ErrorCode errorCode, final Errors errors, final MessageSource messageSource, final Locale locale) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage(), CustomError.of(errors, messageSource, locale));
    }

    public static ErrorResponse of(final MethodArgumentTypeMismatchException e) {
        final String value = e.getValue() == null ? "" : e.getValue().toString();
        final List<CustomError> errors = CustomError.of(e.getName(), value, e.getErrorCode());
        final ErrorCode errorcode = ErrorCode.INVALID_TYPE_VALUE;
        return new ErrorResponse(errorcode.getCode(), errorcode.getMessage(), errors);
    }

    @Getter
    public static class CustomError {
        private String field;
        private String value;
        private String reason;

        public CustomError() {
        }

        public CustomError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<CustomError> of(final String field, final String value, final String reason) {
            final List<CustomError> customErrors = new ArrayList<>();
            customErrors.add(new CustomError(field, value, reason));
            return customErrors;
        }

        private static List<CustomError> of(final Errors errors) {
            final List<FieldError> fieldErrors = errors.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new CustomError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .toList();
        }

        private static List<CustomError> of(final Errors errors, final MessageSource messageSource, final Locale locale) {
            final List<FieldError> fieldErrors = errors.getFieldErrors();
            return fieldErrors.stream()
                    .map(error ->
                            new CustomError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage() == null ? messageSource.getMessage(error, locale) : error.getDefaultMessage()))
                    .toList();
        }
    }
}
