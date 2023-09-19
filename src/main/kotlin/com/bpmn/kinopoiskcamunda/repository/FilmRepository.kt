package com.bpmn.kinopoiskcamunda.repository

import com.bpmn.kinopoiskcamunda.model.Film
import org.springframework.data.jpa.repository.JpaRepository

interface FilmRepository : JpaRepository<Film, Long> {
    fun findByTitle(title: String):Film?
}