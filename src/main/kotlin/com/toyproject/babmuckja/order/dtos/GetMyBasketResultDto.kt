package com.toyproject.babmuckja.order.dtos

import com.toyproject.babmuckja.order.entities.OrderEntity

data class GetMyBasketResultDto (
        val result : MutableList<BasketItemDto>
) {
}