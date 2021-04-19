package com.toyproject.babmuckja.order.dtos

data class AddBasketDto(
    val orderDetailInfo: List<OrderDetailInfo>,
    val email: String,
    val supplierId: Long
) {
}