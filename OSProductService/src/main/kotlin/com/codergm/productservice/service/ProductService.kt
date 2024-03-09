package com.codergm.productservice.service

import com.codergm.productservice.entity.Product
import com.codergm.productservice.model.ProductRequest
import com.codergm.productservice.model.ProductResponse
import com.codergm.productservice.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(val productRepository: ProductRepository) {
    fun addProduct(productRequest: ProductRequest): Long? {
        val product =
            Product(productName = productRequest.name, price = productRequest.price, quantity = productRequest.quantity)
        return productRepository.save(product).productId
    }

    fun getProductById(id: Long): ProductResponse {
        val product = productRepository.findById(id)
            .orElseThrow { RuntimeException("Product with id: ${id} not found") }
        return ProductResponse(
            productName = product.productName,
            productId = product.productId,
            quantity = product.quantity,
            price = product.price
        )
    }
}