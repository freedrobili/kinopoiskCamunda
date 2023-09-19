package com.bpmn.kinopoiskcamunda.delegate

import com.bpmn.kinopoiskcamunda.service.FilmEnrichmentService
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DetermineFilmIdDelegate : JavaDelegate{

    @Autowired
    private lateinit var filmEnrichmentService: FilmEnrichmentService
    override fun execute(execution: DelegateExecution) {
        val keyword = execution.getVariable("keyword") as String

        val filmId = filmEnrichmentService.enrichFilmData(keyword)

        if(filmId != null){
            execution.setVariable("filmId", filmId)
        } else {
            execution.setVariable("filmId", null)
        }
    }
}