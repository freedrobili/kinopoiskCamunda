package com.bpmn.kinopoiskcamunda.dto.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class FilmSearchFilmDto(
        var filmId: Int? = null,
        var nameRu: String? = null,
        var nameEn: String? = null,
        var type: String? = null,
        var year: String? = null,
        var description: String? = null,
        var filmLength: String? = null,
        var countries: List<Country>? = null,
        var genres: List<Genre>? = null,
        var rating: String? = null,
        var ratingVoteCount: Int? = null,
        var posterUrl: String? = null,
        var posterUrlPreview: String? = null
)