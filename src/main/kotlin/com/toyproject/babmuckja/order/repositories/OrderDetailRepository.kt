package com.toyproject.babmuckja.order.repositories

import com.toyproject.babmuckja.order.entities.OrderDetailEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderDetailRepository : JpaRepository<OrderDetailEntity, Long> {
}