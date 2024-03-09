package com.codergm.productservice.repository

import com.codergm.productservice.entity.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository: JpaRepository<Product, Long> {

}