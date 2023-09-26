package com.bpmn.kinopoiskcamunda.controller

import com.bpmn.kinopoiskcamunda.dto.response.FilmSearchFilmDto
import com.bpmn.kinopoiskcamunda.service.ApplicationService
import com.bpmn.kinopoiskcamunda.service.ApplicationUpdateService
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ProcessController(
        private val applicationService: ApplicationService,
        private val applicationUpdateService: ApplicationUpdateService
) {
    @PostMapping("/start-process")
    fun startCamundaProcess(@RequestParam("keyword") keyword: String): Long? {
        return applicationService.startCamundaProcess(keyword)
    }

    @PostMapping("/update-keyword")
    fun updateKeywordAndContinue(
            @RequestParam("applicationId") applicationId: Long,
            @RequestParam("newKeyword") newKeyword: String,
            @Value("\${ev_name}") eventName: String
    ) : FilmSearchFilmDto?{
        return applicationUpdateService.updateKeywordAndContinue(applicationId, newKeyword, eventName)
    }
}
