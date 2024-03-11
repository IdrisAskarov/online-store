package com.codergm.orderservice.enity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "order_details")
data class Order(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long,
    var orderId: Long,
    var quantity: Long,
    var orderDate: Instant,
    @Column(name = "status") var orderStatus: String,
    @Column(name = "total_amount") var amount: Long
)