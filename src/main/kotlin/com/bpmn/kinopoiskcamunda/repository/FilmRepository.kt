package com.bpmn.kinopoiskcamunda.repository

import com.bpmn.kinopoiskcamunda.model.Film
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FilmRepository : JpaRepository<Film, Long> {
}