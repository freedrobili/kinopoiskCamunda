package com.bpmn.kinopoiskcamunda.delegate

import com.bpmn.kinopoiskcamunda.repository.ApplicationRepository
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component

@Component("saveDelegate")
class SaveToDatabaseDelegate(private val applicationRepository: ApplicationRepository) : JavaDelegate {

    override fun execute(execution: DelegateExecution) {
        val applicationId = execution.businessKey.toLong()
        val application = applicationRepository.findById(applicationId)
        if (application.isPresent) {
            val keyword = application.get().keyword
        } else {
            throw IllegalArgumentException("Application not found with id: $applicationId")
        }
    }
}