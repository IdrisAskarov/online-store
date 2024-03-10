package com.codergm.productservice.advice

import com.codergm.productservice.constant.*
import com.fasterxml.jackson.databind.ObjectMapper
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.CodeSignature
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.Objects
import kotlin.time.measureTime

@Aspect
@Component
class LoggingAdvice {


    @Pointcut(value = "within(com.codergm.productservice.service..*)")
    fun servicePointcut() { //Point cut for service layer functions
    }

    @Pointcut(
        value = "within(com.codergm.productservice.controller..*) " +
                "&& (@annotation(org.springframework.web.bind.annotation.GetMapping)" +
                "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
                "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
                "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)))"
    )
    fun controllerRequestPointcut() { //Point cut for controller layer functions
    }

    @Around("servicePointcut()")
    fun serviceLayerLogger(pjp: ProceedingJoinPoint): Any {
        return printLog(pjp, LAYER_SERVICE, "N/A", listOf(), listOf())
    }

    @Around("controllerRequestPointcut()")
    fun controllerLayerGetLogger(pjp: ProceedingJoinPoint): Any {
        val signature = pjp.signature as MethodSignature
        val requestMapping = (signature.declaringType as Class<*>).getAnnotation(RequestMapping::class.java)
        val topUrl =
            if (Objects.nonNull(requestMapping) && requestMapping.value.isNotEmpty()) requestMapping.value[0] else ""
        var urls = arrayOf<String>()
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        val headers = request.headerNames.toList().map { header -> "$header:${request.getHeader(header)}" }
        var requestMethod = ""
        when {
            signature.method.getAnnotation(GetMapping::class.java) != null -> {
                urls = signature.method.getAnnotation(GetMapping::class.java).value
                requestMethod = REQUEST_METHOD_GET
            }

            signature.method.getAnnotation(PostMapping::class.java) != null -> {
                urls = signature.method.getAnnotation(PostMapping::class.java).value
                requestMethod = REQUEST_METHOD_POST
            }

            signature.method.getAnnotation(PutMapping::class.java) != null -> {
                urls = signature.method.getAnnotation(PutMapping::class.java).value
                requestMethod = REQUEST_METHOD_PUT
            }

            signature.method.getAnnotation(DeleteMapping::class.java) != null -> {
                urls = signature.method.getAnnotation(DeleteMapping::class.java).value
                requestMethod = REQUEST_METHOD_DELETE
            }
        }
        return printLog(
            pjp,
            LAYER_CONTROLLER,
            requestMethod,
            if (urls.isNotEmpty()) urls.map { url -> "$topUrl${url}" } else listOf(topUrl),
            headers)
    }

    private fun getParameters(joinPoint: JoinPoint): Map<String, Any> {
        val signature = joinPoint.signature as CodeSignature
        val map = HashMap<String, Any>()
        val parameterNames = signature.parameterNames
        for (i in parameterNames.indices) {
            map[parameterNames[i]] = joinPoint.args[i]
        }
        return map
    }

    private fun printLog(
        pjp: ProceedingJoinPoint,
        layer: String,
        requestMethod: String,
        urls: List<String>,
        headers: List<String>
    ): Any {
        val objectMapper = ObjectMapper()
        val className = pjp.target.javaClass.name
        val method = pjp.signature.name
        var data = objectMapper.writeValueAsString(getParameters(pjp))
        val logger = LoggerFactory.getLogger(className)
        logger.info(
            "$DIRECTION_INPUT, layer: $layer, method invoked: $className.$method(), requestMethod: $requestMethod, " +
                    "urls: $urls, data: $data, headers: $headers"
        )
        val returnObject: Any
        val timeTaken = measureTime { returnObject = pjp.proceed() }
        data = objectMapper.writeValueAsString(returnObject)
        logger.info(
            "$DIRECTION_OUTPUT, layer: $layer, method invoked: $className.$method(), requestMethod: $requestMethod, " +
                    "urls: $urls, data: $data"
        )
        logger.info("Method $className.$method() executed in $timeTaken")
        return returnObject
    }
}