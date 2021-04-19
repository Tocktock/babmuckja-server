package com.toyproject.babmuckja.billing.dtos

import com.toyproject.babmuckja.billing.constants.BillState
import com.toyproject.babmuckja.billing.constants.PaymentMethod
import com.toyproject.babmuckja.order.entities.OrderDetailEntity
import java.time.LocalDateTime

data class GetMyBillingResultDto(
    val billState : BillState,
    val createAt : LocalDateTime,
    val paymentMethod: PaymentMethod,
    val billingPrice : Int,
    val orderDetails : List<BillingOrderDetailInfo>
) {
}