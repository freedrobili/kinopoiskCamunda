package com.bpmn.kinopoiskcamunda.aspects

import com.bpmn.kinopoiskcamunda.annotations.MetricTiming
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import java.time.Duration

@Aspect
@Component
class MetricAspect(private val meterRegistry: MeterRegistry) {

    @Around("@annotation(metricTiming)")
    fun measureExecutionTime(joinPoint: ProceedingJoinPoint, metricTiming: MetricTiming): Any? {
        val methodName = joinPoint.signature.name
        val timer = Timer.builder(metricTiming.value)
            .description("Execution time of $methodName")
            .tags("method", methodName)
            .register(meterRegistry)

        val startTime = System.nanoTime()
        val endTime = System.nanoTime()
        val duration = Duration.ofNanos(endTime-startTime)
        timer.record(duration)

        return null
    }
}




