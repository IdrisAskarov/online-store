package com.codergm.productservice.model

data class LogData(
    private var direction: String,
    var layer: String,
    var requestMethod: String = "N/A",
    var url: String = "N/A",
    var className: String,
    var method: String,
    var data: String
)