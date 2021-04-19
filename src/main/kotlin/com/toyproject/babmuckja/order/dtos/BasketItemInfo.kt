package com.toyproject.babmuckja.order.dtos

data class BasketItemInfo (
    val quentity : Int,
    val productId : Long,
    val productName : String,
    val productPrice : Int,
){
}