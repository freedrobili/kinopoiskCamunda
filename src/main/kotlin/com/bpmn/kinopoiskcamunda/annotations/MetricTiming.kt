package com.bpmn.kinopoiskcamunda.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MetricTiming(val value: String = "")
