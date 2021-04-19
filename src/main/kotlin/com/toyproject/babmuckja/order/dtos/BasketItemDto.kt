package com.toyproject.babmuckja.order.dtos

import com.toyproject.babmuckja.order.entities.OrderDetailEntity
import com.toyproject.babmuckja.product.entities.ProductEntity

data class BasketItemDto (
    val orderId : Long,
    val supplierId : Long,
    val supplierName : String,
    val items : List<BasketItemInfo>
        ){
}