package com.bpmn.kinopoiskcamunda.delegate

import com.bpmn.kinopoiskcamunda.model.Film
import com.bpmn.kinopoiskcamunda.repository.ApplicationRepository
import com.bpmn.kinopoiskcamunda.repository.FilmRepository
import com.bpmn.kinopoiskcamunda.service.FilmEnrichmentService
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component

@Component("saveDelegate")
class SaveToDatabaseDelegate(
        private val applicationRepository: ApplicationRepository,
        private val filmEnrichmentService: FilmEnrichmentService,
        private val filmRepository: FilmRepository
) : JavaDelegate {

    override fun execute(execution: DelegateExecution) {
        val applicationId = execution.businessKey.toLong()
        val application = applicationRepository.findById(applicationId)

        if (application.isPresent) {
            val keyword = application.get().keyword

            val filmInfo = filmEnrichmentService.enrichFilmDto(keyword)

            if (filmInfo != null) {
                val film = Film(
                        filmId = filmInfo.filmId,
                        title = filmInfo.nameRu ?: "",
                        releaseYear = filmInfo.year?.toIntOrNull() ?: 0,
                        genre = filmInfo.genres.toString(),
                        rating = filmInfo.rating?.toDoubleOrNull() ?: 0.0
                )
                val savedFilm = filmRepository.save(film)

                val savedApplication = application.get()
                savedApplication.film = savedFilm

                applicationRepository.save(savedApplication)
            } else {
                throw IllegalArgumentException("Film data for keyword '$keyword' is not available.")
            }
        } else {
            throw IllegalArgumentException("Application not found with id: $applicationId")
        }
    }
}
