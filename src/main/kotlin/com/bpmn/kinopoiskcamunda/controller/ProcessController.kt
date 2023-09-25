package com.bpmn.kinopoiskcamunda.controller

import com.bpmn.kinopoiskcamunda.model.Application
import com.bpmn.kinopoiskcamunda.model.ApplicationStatus
import com.bpmn.kinopoiskcamunda.repository.ApplicationRepository
import com.bpmn.kinopoiskcamunda.repository.FilmRepository
import org.camunda.bpm.engine.RuntimeService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api")
class ProcessController(
        private val runtimeService: RuntimeService,
        private val applicationRepository: ApplicationRepository,
        private val filmRepository: FilmRepository
) {
    @PostMapping("/start-process")
    fun startCamundaProcess(@RequestParam("keyword") keyword: String): String {
        try {
            val bpmnProcessKey = "File_Process"

            val application = Application(
                    status = ApplicationStatus.NEW,
                    creationData = LocalDateTime.now(),
                    updateDate = LocalDateTime.now(),
                    keyword = keyword
            )

            val savedApplication = applicationRepository.save(application)

            val applicationId = savedApplication.id

            if (applicationId != null) {
                runtimeService.createProcessInstanceByKey(bpmnProcessKey)
                        .businessKey(applicationId.toString())
                        .setVariable("keyword", keyword)
                        .execute()
                return "OK"
            } else {
                throw IllegalArgumentException(" applicationId is required")
            }
        } catch (ex: Exception) {
            throw ex
        }
    }

    @PostMapping("/update-keyword")
    fun updateKeywordAndContinue(
            @RequestParam("applicationId") applicationId: Long,
            @RequestParam("newKeyword") newKeyword: String
    ): String {
        try {
            val application = applicationRepository.findById(applicationId)

            if (application.isPresent) {

                val existingApplication = application.get()
                existingApplication.keyword = newKeyword

                applicationRepository.save(existingApplication)

                val eventName = "MessageName"
                val processInstance = runtimeService.createProcessInstanceQuery()
                        .processInstanceBusinessKey(applicationId.toString())
                        .singleResult()

                if (processInstance != null) {
                    val executionId = processInstance.id

                    val eventSubscription = runtimeService.createEventSubscriptionQuery()
                            .processInstanceId(executionId)
                            .eventName(eventName)
                            .singleResult()

                    if (eventSubscription != null) {
                        val processVariable = mapOf("keyword" to newKeyword)
                        runtimeService.messageEventReceived(eventSubscription.eventName, eventSubscription.executionId, processVariable)
                        return "OK"
                    } else {
                        throw IllegalArgumentException("Event subscription not found for executionId $executionId and eventName $eventName")
                    }
                } else {
                    throw IllegalArgumentException("Failed to start the process for application with ID $applicationId")
                }
            } else {
                throw IllegalArgumentException("Application with ID $applicationId not found")
            }
        } catch (ex: Exception) {
            throw ex
        }
    }
}
