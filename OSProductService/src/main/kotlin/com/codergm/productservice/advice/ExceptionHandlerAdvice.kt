package com.codergm.productservice.advice

import com.codergm.productservice.exception.ProductException
import com.codergm.productservice.model.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class ExceptionHandlerAdvice : ResponseEntityExceptionHandler() {

    @ExceptionHandler(ProductException::class)
    fun handleProductServiceException(exception: ProductException): ResponseEntity<ErrorResponse> {
        val logger = LoggerFactory.getLogger(ProductException::class.java)
        logger.error(exception.stackTraceToString())
        return status(HttpStatus.NOT_FOUND).body(exception.message?.let {
            ErrorResponse(
                it,
                exception.errorCode,
                LocalDateTime.now(),
                exception.stackTraceToString()
            )
        })
    }
}