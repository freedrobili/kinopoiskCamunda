package com.bpmn.kinopoiskcamunda.model

import javax.persistence.*

@Entity
@Table(name = "film")
data class Film(
        @Id
        @JoinColumn(name = "film_id")
        val filmId: Int? = null,

        @Column(nullable = false)
        val title: String,

        @Column(nullable = false)
        val releaseYear: Int,

        @Column(nullable = false)
        val genre: String,

        @Column(nullable = false)
        val rating: Double
)
