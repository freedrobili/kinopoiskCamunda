package com.bpmn.kinopoiskcamunda.kafka

import com.bpmn.kinopoiskcamunda.dto.response.FilmSearchFilmDto
import com.bpmn.kinopoiskcamunda.exception.ApplicationNotFoundException
import com.bpmn.kinopoiskcamunda.repository.ApplicationRepository
import com.bpmn.kinopoiskcamunda.service.FilmEnrichmentService
import org.camunda.bpm.engine.RuntimeService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class KafkaConsumerService(
    private val runtimeService: RuntimeService,
    private val applicationRepository: ApplicationRepository,
    private val filmEnrichmentService: FilmEnrichmentService,
) {
    @KafkaListener(topics = ["quickstart"])
    fun listen(message: String): FilmSearchFilmDto? {
        val parts = message.split(",")
        val eventName = "MessageName"

        val applicationId = parts[0].toLong()
        val newKeyword = parts[1]

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