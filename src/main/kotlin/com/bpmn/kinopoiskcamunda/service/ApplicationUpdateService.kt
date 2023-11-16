package com.bpmn.kinopoiskcamunda.service

import com.bpmn.kinopoiskcamunda.dto.response.FilmSearchFilmDto
import com.bpmn.kinopoiskcamunda.exception.ApplicationNotFoundException
import com.bpmn.kinopoiskcamunda.repository.ApplicationRepository
import org.camunda.bpm.engine.RuntimeService
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestParam

@Service
class ApplicationUpdateService(
        private val runtimeService: RuntimeService,
        private val applicationRepository: ApplicationRepository,
        private val filmEnrichmentService: FilmEnrichmentService
) {
    fun updateKeywordAndContinue(
            @RequestParam("applicationId") applicationId: Long,
            @RequestParam("newKeyword") newKeyword: String,
            @Value("\${ev_name}") eventName: String
    ): FilmSearchFilmDto? {

        val application = applicationRepository.findByIdOrNull(applicationId)
                ?: throw ApplicationNotFoundException("Application not found with ID: $applicationId")

        application.let {

            application.keyword = newKeyword
            applicationRepository.save(application)


            val processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceBusinessKey(applicationId.toString())
                    .active()
                    .list()

            processInstance?.let {
                val executionId = processInstance[0].id

                val eventSubscription = runtimeService.createEventSubscriptionQuery()
                        .processInstanceId(executionId)
                        .eventName(eventName)
                        .singleResult()

                eventSubscription?.let {
                    val processVariable = mapOf("keyword" to newKeyword)
                    runtimeService.messageEventReceived(
                        eventSubscription.eventName,
                        eventSubscription.executionId,
                        processVariable
                    )

                    return filmEnrichmentService.enrichFilmDto(newKeyword)
                }
            }
        }
        return null
    }
}