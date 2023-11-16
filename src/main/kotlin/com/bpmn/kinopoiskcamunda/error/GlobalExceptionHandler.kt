package com.bpmn.kinopoiskcamunda.error

import com.bpmn.kinopoiskcamunda.dto.response.ApiResponse
import com.bpmn.kinopoiskcamunda.exception.ApplicationNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationNotFoundException::class)
    fun handlerApplicationNotFound(ex: ApplicationNotFoundException):ResponseEntity<ApiResponse>{
        val apiResponse = ApiResponse("ApplicationId not found", 404)
        return ResponseEntity(apiResponse, HttpStatus.NOT_FOUND)
    }
}