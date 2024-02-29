package com.spring.guide.retry.application;

import com.spring.guide.retry.domain.Result;

public interface CollectAuditLogService {
    Result retry();
}
