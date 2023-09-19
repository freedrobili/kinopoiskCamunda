package com.bpmn.kinopoiskcamunda.model

import javax.persistence.*

@Entity
@Table(name = "film")
data class Film(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @Column(nullable = false)
        val title: String,

        @Column(nullable = false)
        val director: String,

        @Column(nullable = false)
        val releaseYear: Int,

        @Column(nullable = false)
        val genre: String,

        @Column(nullable = false)
        val rating: Double
)
