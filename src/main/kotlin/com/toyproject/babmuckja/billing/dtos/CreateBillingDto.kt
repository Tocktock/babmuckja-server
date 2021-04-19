package com.toyproject.babmuckja.billing.dtos

import com.toyproject.babmuckja.billing.constants.PaymentMethod

data class CreateBillingDto(
    val orderIds: List<Long>,
    val email: String,
    val paymentMethod: PaymentMethod
) {
}