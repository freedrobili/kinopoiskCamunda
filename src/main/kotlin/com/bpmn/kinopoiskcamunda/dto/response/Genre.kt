package com.bpmn.kinopoiskcamunda.dto.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Genre(
        var genre: String? = null
)