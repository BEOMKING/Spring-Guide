package com.spring.guide.retry.api;

import com.spring.guide.retry.application.RetryService;
import com.spring.guide.retry.domain.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class RetryController {
    private final RetryService retryService;

    public RetryController(RetryService retryService) {
        this.retryService = retryService;
    }

    @GetMapping("/retry1")
    public void retry1() {
        retryService.retry1();
    }

    @GetMapping("/retry2")
    public Result retry2() {
        return retryService.retry2();
    }
}
