package com.toyproject.babmuckja.order.repositories

import com.toyproject.babmuckja.order.entities.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<OrderEntity, Long> {
}