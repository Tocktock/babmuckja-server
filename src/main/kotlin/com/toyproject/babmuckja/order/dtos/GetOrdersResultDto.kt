package com.toyproject.babmuckja.order.dtos

import com.toyproject.babmuckja.order.entities.OrderEntity

data class GetOrdersResultDto(
    val orders: List<OrderEntity>
) {
}