package com.bpmn.kinopoiskcamunda.controller

import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.runtime.ProcessInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ProcessController(
        @Autowired
        private val runtimeService: RuntimeService
) {
    @PostMapping("/start-process")
    fun startCamundaProcess(@RequestParam("applicationId") applicationId: Long?): ProcessInstance {
        try {
            val bpmnProcessKey = "File_Process"

            if (applicationId != null){
                val processInstance = runtimeService.createProcessInstanceByKey(bpmnProcessKey)
                        .businessKey(applicationId.toString())
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