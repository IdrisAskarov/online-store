package com.codergm.productservice.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Product(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var productId: Long = 0,
    @Column(name = "PRODUCT_NAME") var productName: String = "",
    var price: Long = 0,
    var quantity: Int = 0
)