package com.bpmn.kinopoiskcamunda.dto.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class FilmSearchResponse(
        var keyword: String? = null,
        var pagesCount: Int? = null,
        var searchFilmsCountResult: Int? = null,
        var films: List<FilmSearchFilmDto>? = null
)







