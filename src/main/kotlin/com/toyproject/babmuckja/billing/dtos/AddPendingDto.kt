package com.toyproject.babmuckja.billing.dtos

data class AddPendingDto (
    val email : String,
    val orderIds: List<Long>,
    ){
}