package com.toyproject.babmuckja.supplier.dtos

import com.toyproject.babmuckja.product.entities.ProductEntity
import com.toyproject.babmuckja.supplier.entities.SupplierEntity

class GetSupplierDetailResultDto(
    val supplier: SupplierEntity,
    val products: List<ProductEntity>
) {

}