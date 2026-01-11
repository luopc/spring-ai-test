package com.luopc.platform.cloud.service.ai;

import dev.langchain4j.http.client.HttpClient;
import dev.langchain4j.http.client.HttpClientBuilder;

import java.time.Duration;

public class CustomHttpClientBuilder implements HttpClientBuilder {
    @Override
    public Duration connectTimeout() {
        return null;
    }

    @Override
    public HttpClientBuilder connectTimeout(Duration duration) {
        return null;
    }

    @Override
    public Duration readTimeout() {
        return null;
    }

    @Override
    public HttpClientBuilder readTimeout(Duration duration) {
        return null;
    }

    @Override
    public HttpClient build() {
        return null;
    }
}
