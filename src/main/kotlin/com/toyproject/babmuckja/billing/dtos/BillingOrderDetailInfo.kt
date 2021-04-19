package com.toyproject.babmuckja.billing.dtos

class BillingOrderDetailInfo(
    val productName : String,
    val quentity : Int,
    val price : Int,
    val supplierName : String,
    val supplierId : Long
) {
}