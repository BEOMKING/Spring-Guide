package com.spring.guide.domain.member.exception;

import com.spring.guide.global.error.exception.BusinessException;
import com.spring.guide.global.error.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ResourceConflictException extends BusinessException {

        public ResourceConflictException(final String message, final ErrorCode errorCode) {
            super(message, errorCode);
        }
}
