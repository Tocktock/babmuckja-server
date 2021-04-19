package com.toyproject.babmuckja.order.dtos

class GetOrderInfoListDto(
    val orderId : Long,
    val supplierId : Long,
    val supplierName : String,
    val items : List<OrderItemInfo>
) {
}