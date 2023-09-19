package com.bpmn.kinopoiskcamunda.repository

import com.bpmn.kinopoiskcamunda.model.Application
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ApplicationRepository : JpaRepository<Application, Long>