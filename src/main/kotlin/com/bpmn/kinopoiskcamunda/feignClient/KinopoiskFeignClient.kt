package com.bpmn.kinopoiskcamunda.feignClient

import com.bpmn.kinopoiskcamunda.dto.response.FilmSearchResponse
import feign.RequestInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "kinopoisk", url = "\${kinopoisk.api.url}", configuration = [KinopoiskFeignClient.FeignConfiguration::class])
interface KinopoiskFeignClient {
    @GetMapping("/v2.1/films/search-by-keyword")
    fun searchFilm(@RequestParam keyword: String): FilmSearchResponse

    class FeignConfiguration {
        @Bean
        fun requestInterceptor(@Value("\${X-API-KEY}") kinopoiskHDId: String): RequestInterceptor {
            return RequestInterceptor { template ->
                template.header("X-API-KEY", kinopoiskHDId)
            }
        }
    }
}