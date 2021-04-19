package com.toyproject.babmuckja.supplier.dtos

data class CreateSupplierDto(
    val supplierName: String,
    val location: String,
    val contactInfo: String,
    val category: String,
    val longitude: Double,
    val latitude: Double
) {

}