package com.bpmn.kinopoiskcamunda.delegate

import com.bpmn.kinopoiskcamunda.service.FilmEnrichmentService
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component

@Component("filmDelegate")
class DetermineFilmIdDelegate(private val filmEnrichmentService: FilmEnrichmentService) : JavaDelegate{

    override fun execute(execution: DelegateExecution) {
        val keyword = execution.getVariable("keyword") as String

        val filmId = filmEnrichmentService.enrichFilmData(keyword)
        execution.setVariable("filmId", filmId)
    }
}