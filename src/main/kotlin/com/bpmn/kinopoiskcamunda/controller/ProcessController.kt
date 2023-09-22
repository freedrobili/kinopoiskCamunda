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
        private val kinopoiskFeignClient: KinopoiskFeignClient
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
    
    @PostMapping("/request-precise-title")
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
    }
}