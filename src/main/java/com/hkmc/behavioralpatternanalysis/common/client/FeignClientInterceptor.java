package com.hkmc.behavioralpatternanalysis.common.client;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class FeignClientInterceptor {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.removeHeader("Content-length")
                    .removeHeader("content-length");
        };
    }
}
