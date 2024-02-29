package com.spring.guide.retry.api;

import com.spring.guide.retry.application.CollectAuditLogService;
import com.spring.guide.retry.domain.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AuditLogController {
    private final CollectAuditLogService collectAuditLogService;

    public AuditLogController(CollectAuditLogService collectAuditLogService) {
        this.collectAuditLogService = collectAuditLogService;
    }

    @GetMapping("/collect")
    public Result collectAuditLog() {
        return collectAuditLogService.retry();
    }
}
