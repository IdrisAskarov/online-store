package com.codergm.orderservice.repository

import com.codergm.orderservice.enity.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository: JpaRepository<Order, Long>