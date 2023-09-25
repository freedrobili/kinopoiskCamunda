package com.bpmn.kinopoiskcamunda.service

import com.bpmn.kinopoiskcamunda.feignClient.KinopoiskFeignClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FilmEnrichmentService(
    @Autowired
    private val kinopoiskFeignClient: KinopoiskFeignClient,
){
    fun enrichFilmData(keyword: String): Int? {
        val filmSearchResponse = kinopoiskFeignClient.searchFilm(keyword)
        val films = filmSearchResponse.films
        if (!films.isNullOrEmpty()) {
            val filmInfo = films[0]
            return if (keyword == filmInfo.nameEn) {
                filmInfo.filmId
            } else {
                null
            }
        }
        return null
    }
}