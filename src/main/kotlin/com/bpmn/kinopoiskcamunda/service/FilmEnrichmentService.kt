package com.bpmn.kinopoiskcamunda.service

import com.bpmn.kinopoiskcamunda.dto.response.FilmSearchFilmDto
import com.bpmn.kinopoiskcamunda.feignClient.KinopoiskFeignClient
import com.bpmn.kinopoiskcamunda.repository.FilmRepository
import org.springframework.stereotype.Service

@Service
class FilmEnrichmentService(
        private val kinopoiskFeignClient: KinopoiskFeignClient
) {
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

    fun enrichFilmDto(keyword: String): FilmSearchFilmDto? {
        val filmSearchResponse = kinopoiskFeignClient.searchFilm(keyword)
        val films = filmSearchResponse.films

        if (!films.isNullOrEmpty()) {
            val filmInfo = films[0]
            return FilmSearchFilmDto(
                    filmId = filmInfo.filmId,
                    nameRu = filmInfo.nameRu,
                    nameEn = filmInfo.nameEn,
                    type = filmInfo.type,
                    year = filmInfo.year,
                    description = filmInfo.description,
                    filmLength = filmInfo.filmLength,
                    countries = filmInfo.countries,
                    genres = filmInfo.genres,
                    rating = filmInfo.rating,
                    ratingVoteCount = filmInfo.ratingVoteCount,
                    posterUrl = filmInfo.posterUrl,
                    posterUrlPreview = filmInfo.posterUrlPreview
            )
        }
        return null
    }
}