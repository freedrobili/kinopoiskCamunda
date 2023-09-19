package com.bpmn.kinopoiskcamunda.config

import feign.RequestInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignConfiguration {
    private val kinopoiskHDId = "4824a95e60a7db7e86f14137516ba590"
    @Bean
    fun requestInterceptor() : RequestInterceptor {
        return RequestInterceptor { template ->
            template.header("kinopoiskId", kinopoiskHDId)
        }
    }
}