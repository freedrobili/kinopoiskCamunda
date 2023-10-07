package com.bpmn.kinopoiskcamunda

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableProcessApplication
@SpringBootApplication
@EnableFeignClients
class KinopoiskCamundaApplication

fun main(args: Array<String>) {
    runApplication<KinopoiskCamundaApplication>(*args)
}
