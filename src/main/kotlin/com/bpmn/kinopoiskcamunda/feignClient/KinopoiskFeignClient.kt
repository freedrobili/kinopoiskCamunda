package com.bpmn.kinopoiskcamunda.feignClient

import com.bpmn.kinopoiskcamunda.dto.response.FilmInfoResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "kinopoiskClient", url = "https://kinopoiskapiunofficial.tech/documentation/api/#/")
interface KinopoiskFeignClient {
    @GetMapping("/search")
    fun searchFilm(@RequestParam keyword: String): List<FilmInfoResponse>
}