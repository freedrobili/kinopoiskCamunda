package com.bpmn.kinopoiskcamunda.delegate

import com.bpmn.kinopoiskcamunda.model.Application
import com.bpmn.kinopoiskcamunda.model.ApplicationStatus
import com.bpmn.kinopoiskcamunda.repository.ApplicationRepository
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SaveToDatabaseDelegate : JavaDelegate {

    private lateinit var applicationRepository: ApplicationRepository
    override fun execute(execution: DelegateExecution) {
        val keyword = execution.getVariable("keyword") as String
        val filmId = execution.getVariable("filmId") as Long


        val application = Application(
                status = ApplicationStatus.NEW,
                creationData = LocalDateTime.now(),
                updateDate = LocalDateTime.now(),
                keyword = keyword
        )

        val savedApplication = applicationRepository.save(application)

        val applicationId = savedApplication.id

        execution.setVariable("businessKey",applicationId.toString())
    }

}