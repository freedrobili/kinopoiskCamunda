package com.bpmn.kinopoiskcamunda.controller

import com.bpmn.kinopoiskcamunda.model.Application
import com.bpmn.kinopoiskcamunda.model.ApplicationStatus
import com.bpmn.kinopoiskcamunda.repository.ApplicationRepository
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.runtime.ProcessInstance
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api")
class ProcessController(
        private val runtimeService: RuntimeService,
        private val applicationRepository: ApplicationRepository
) {
    @PostMapping("/start-process")
    fun startCamundaProcess(@RequestParam("keyword") keyword: String): ProcessInstance {
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

            if (applicationId != null){
                val processInstance = runtimeService.createProcessInstanceByKey(bpmnProcessKey)
                        .businessKey(applicationId.toString())
                        .setVariable("keyword", keyword)
                        .execute()
                return processInstance
            } else {
                throw IllegalArgumentException(" applicationId is required")
            }
        } catch (ex: Exception) {
            throw ex
        }
    }
}