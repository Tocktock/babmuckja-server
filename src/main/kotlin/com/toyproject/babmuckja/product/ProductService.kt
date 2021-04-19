package com.toyproject.babmuckja.product

import com.toyproject.babmuckja.order.dtos.AddBasketDto
import com.toyproject.babmuckja.product.entities.ProductEntity
import com.toyproject.babmuckja.product.repositories.ProductRepository
import com.toyproject.babmuckja.product.repositories.QProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(
    val productRepository: ProductRepository,
    val qProductRepository: QProductRepository
) {

    fun getProductsByIds(productIds: List<Long>): List<ProductEntity> {
        return productRepository.findAllById(productIds)
    }

    fun seedProduct(products: MutableSet<ProductEntity>) {
        productRepository.saveAll(products)
    }

    //valiate product wtih supplier
    fun validateProduct(addBasketDto: AddBasketDto): Boolean {
        val productIds = qProductRepository.getProductIdsBySupplierId(addBasketDto.supplierId)
        addBasketDto.orderDetailInfo.forEach {
            if (!productIds.contains(it.productId))
                return false
        }
        return true
    }
}