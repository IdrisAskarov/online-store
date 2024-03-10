package com.codergm.productservice.exception

class ProductException(message: String?, errorCode: String) : RuntimeException(message) {

    var errorCode: String  = ""

    init {
        this.errorCode = errorCode
    }

}