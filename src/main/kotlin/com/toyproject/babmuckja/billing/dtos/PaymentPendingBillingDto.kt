package com.toyproject.babmuckja.billing.dtos

import com.toyproject.babmuckja.billing.constants.PaymentMethod

class PaymentPendingBillingDto(
    val billId : Long,
    val paymentMethod: PaymentMethod
) {
}