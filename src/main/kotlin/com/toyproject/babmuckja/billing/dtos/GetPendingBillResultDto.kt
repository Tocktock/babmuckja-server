package com.toyproject.babmuckja.billing.dtos

import java.time.LocalDateTime

class GetPendingBillResultDto (
    val billId : Long,
    val price : Int,
    val createAt : LocalDateTime,
    val deadline : LocalDateTime
        ) {
}