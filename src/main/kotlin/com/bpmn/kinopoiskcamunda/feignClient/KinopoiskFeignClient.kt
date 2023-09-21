package com.bpmn.kinopoiskcamunda.feignClient

import com.bpmn.kinopoiskcamunda.dto.response.FilmInfoResponse
import feign.RequestInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "kinopoiskClient", url = "\${kinopoisk.api.url}")
interface KinopoiskFeignClient {
    @GetMapping("/v2.1/films/search-by-keyword")
    fun searchFilm(@RequestParam keyword: String): List<FilmInfoResponse>

    class FeignConfiguration {
        @Bean
        fun requestInterceptor(@Value("\${kinopoiskHDId}") kinopoiskHDId: String): RequestInterceptor {
            return RequestInterceptor { template ->
                template.header("kinopoiskId", kinopoiskHDId)
            }
        }
    }
}