package com.codergm.productservice.model

import java.time.LocalDateTime

data class ErrorResponse(
    val errorMessage: String,
    val errorCode: String,
    val errorTimestamp: LocalDateTime,
    val stackTrace: String
)