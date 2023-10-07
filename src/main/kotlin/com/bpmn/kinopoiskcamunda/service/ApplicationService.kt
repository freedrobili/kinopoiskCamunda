package com.bpmn.kinopoiskcamunda.service

import com.bpmn.kinopoiskcamunda.dto.enam.BpmnProcessKey
import com.bpmn.kinopoiskcamunda.model.Application
import com.bpmn.kinopoiskcamunda.model.ApplicationStatus
import com.bpmn.kinopoiskcamunda.repository.ApplicationRepository
import org.camunda.bpm.engine.RuntimeService
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDateTime

@Service
class ApplicationService(
        private val runtimeService: RuntimeService,
        private val applicationRepository: ApplicationRepository
) {
    fun startCamundaProcess(@RequestParam("keyword") keyword: String): Long? {
        try {
            val bpmnProcessKey = BpmnProcessKey.FILE_PROCESS

            val application = Application(
                    status = ApplicationStatus.NEW,
                    creationData = LocalDateTime.now(),
                    updateDate = LocalDateTime.now(),
                    keyword = keyword
            )

            val savedApplication = applicationRepository.save(application).id

            savedApplication?.let {
                runtimeService.createProcessInstanceByKey(bpmnProcessKey.value)
                        .businessKey(savedApplication.toString())
                        .setVariable("keyword", keyword)
                        .execute()
            }
            return savedApplication
        } catch (ex: Exception) {
            throw ex
        }
    }
}