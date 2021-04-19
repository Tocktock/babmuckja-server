package com.toyproject.babmuckja.order.dtos

data class ChangeOrderDetailDto(
    val orderId : Long,
    val productId : Long,
    val quentity : Int
) {
}