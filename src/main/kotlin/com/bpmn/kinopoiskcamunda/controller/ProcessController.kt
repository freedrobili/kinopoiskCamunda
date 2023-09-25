package com.bpmn.kinopoiskcamunda.controller

import com.bpmn.kinopoiskcamunda.dto.response.FilmSearchFilmDto
import com.bpmn.kinopoiskcamunda.dto.response.FilmSearchResponse
import com.bpmn.kinopoiskcamunda.feignClient.KinopoiskFeignClient
import com.bpmn.kinopoiskcamunda.model.Application
import com.bpmn.kinopoiskcamunda.model.ApplicationStatus
import com.bpmn.kinopoiskcamunda.model.Film
import com.bpmn.kinopoiskcamunda.repository.ApplicationRepository
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

/*    @PostMapping("/request-precise-title")
    fun requestPreciseTitle(@RequestParam("clarification") clarification: String): FilmSearchResponse? {
        try {
            val application = applicationRepository.findByKeyword(clarification)

            if (application != null) {
                return FilmSearchResponse(
                        keyword = clarification,
                        pagesCount = 1,
                        searchFilmsCountResult = 1,
                        films = listOf(FilmSearchFilmDto(nameEn = clarification))
                )
            } else {
                throw IllegalArgumentException("Application with clarification $clarification not found")
            }
        } catch (ex: Exception) {
            throw ex
        }
    }*/

    @PostMapping("/update-keyword")
    /*fun updateKeywordAndContinue(
            @RequestParam("applicationId") applicationId: Long,
            @RequestParam("newKeyword") newKeyword: String
    ): String {
        try {
            val application = applicationRepository.findById(applicationId)

            if (application.isPresent) {
                val keyword = application.get().keyword

                val existingApplication = application.get()
                existingApplication.keyword = newKeyword
//                val updatedApplication = existingApplication.copy(keyword = newKeyword)

                applicationRepository.save(existingApplication)

                //процесс по businessKey
                val processInstance = runtimeService.createProcessInstanceQuery()
                        .processInstanceBusinessKey(applicationId.toString())
                        .singleResult()


                if (processInstance != null) {
                    val executionId = processInstance.id

                    val pi = runtimeService.startProcessInstanceByKey("")
                    val eventName = "MessageName"

                    val subscription = runtimeService.createEventSubscriptionQuery()
                            .processInstanceId(pi.id)
                            .eventType("message")
                            .singleResult()

//                    val eventName = "Уточнить название"
//                    val eventSubscription = runtimeService.createEventSubscriptionQuery()
//                            .executionId(executionId)
//                            .processInstanceId(executionId)
//                            .eventName(eventName)
//                            .singleResult()

//                    if (eventSubscription != null) {
                    if (subscription != null) {
                        runtimeService.messageEventReceived(eventName, executionId)
                        return "OK"
                    } else{
                        throw IllegalArgumentException("Event subscription nor found for executionId $executionId and eventName $eventName")
                    }

                } else {
                    throw IllegalArgumentException("Process not found for application with ID $applicationId")
                }
            } else {
                throw IllegalArgumentException("Application with ID $applicationId not found")
            }
        } catch (ex: Exception) {
            throw ex
        }
    }*/
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
                        runtimeService.messageEventReceived(eventSubscription.eventName, eventSubscription.executionId)
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
