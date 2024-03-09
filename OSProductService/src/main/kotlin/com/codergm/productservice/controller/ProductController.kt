package com.codergm.productservice.controller

import com.codergm.productservice.model.ProductRequest
import com.codergm.productservice.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/product")
class ProductController(val productService: ProductService) {

    @PostMapping("")
    fun addProduct(@RequestBody productRequest: ProductRequest) =
        status(HttpStatus.CREATED).body(productService.addProduct(productRequest))


    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long) = ok(productService.getProductById(id))
}