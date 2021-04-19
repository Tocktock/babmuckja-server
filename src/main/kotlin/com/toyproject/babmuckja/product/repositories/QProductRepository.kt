package com.toyproject.babmuckja.product.repositories

import com.querydsl.jpa.impl.JPAQueryFactory
import com.toyproject.babmuckja.product.entities.QProductEntity.productEntity
import org.springframework.stereotype.Repository

@Repository
class QProductRepository(
    val query: JPAQueryFactory
) {
    fun getProductIdsBySupplierId(supplierId: Long): List<Long> {
        val list = query.select(productEntity.id)
            .from(productEntity)
            .where(productEntity.supplier.id.eq(supplierId))
            .fetch()
        return list
    }
}