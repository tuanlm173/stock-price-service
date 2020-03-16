package com.tuanle.stockintelligence.config;

import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpConfig {

    private static final Logger logger = LoggerFactory.getLogger(HttpConfig.class);

    @Bean(name = "http-protocol")
    public OkHttpClient getClient() {
        return new OkHttpClient();
    }
}
