package com.bpmn.kinopoiskcamunda.controller

import com.bpmn.kinopoiskcamunda.dto.response.ApiResponse
import com.bpmn.kinopoiskcamunda.service.ApplicationService
import com.bpmn.kinopoiskcamunda.service.ApplicationUpdateService
import io.swagger.v3.oas.annotations.Operation
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
    @Operation(summary = "Start a Camunda process")
    @PostMapping("/start-process")
    fun startCamundaProcess(@RequestParam("keyword") keyword: String): ApiResponse {
        val businessKey = applicationService.startCamundaProcess(keyword)
        return ApiResponse(businessKey?.toString() ?: "", 0)
    }

    @Operation(summary = "Update keyword and continue")
    @PostMapping("/update-keyword")
    fun updateKeywordAndContinue(
            @RequestParam("applicationId") applicationId: Long,
            @RequestParam("newKeyword") newKeyword: String,
            @Value("\${ev_name}") eventName: String
    ) : ApiResponse{
        applicationUpdateService.updateKeywordAndContinue(applicationId, newKeyword, eventName)
        return ApiResponse()
    }
}
