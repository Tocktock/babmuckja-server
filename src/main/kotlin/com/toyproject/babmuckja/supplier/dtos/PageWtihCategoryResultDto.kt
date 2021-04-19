package com.toyproject.babmuckja.supplier.dtos

import com.toyproject.babmuckja.supplier.entities.SupplierEntity

data class PageWtihCategoryResultDto(
    val supplierList: List<SupplierEntity>
) {

}