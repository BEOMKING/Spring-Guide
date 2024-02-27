package com.spring.guide.retry.domain;

import lombok.Getter;

@Getter
public class Result {
    Integer retryCount;
    Boolean scuccess;
    SearchResult searchResult;
    String detail;

    public Result() {
    }

    public Result(Integer retryCount, Boolean scuccess, SearchResult searchResult, String detail) {
        this.retryCount = retryCount;
        this.scuccess = scuccess;
        this.searchResult = searchResult;
        this.detail = detail;
    }

    @Getter
    public static class SearchResult {
        private String token;
        private boolean subscribe;

        public SearchResult() {
        }

        public SearchResult(String token, boolean subscribe) {
            this.token = token;
            this.subscribe = subscribe;
        }
    }
}
