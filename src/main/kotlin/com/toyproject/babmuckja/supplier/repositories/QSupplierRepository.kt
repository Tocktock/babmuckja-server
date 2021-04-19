package com.toyproject.babmuckja.supplier.repositories

import com.querydsl.jpa.impl.JPAQueryFactory
import com.toyproject.babmuckja.product.entities.QProductEntity
import com.toyproject.babmuckja.supplier.entities.QSupplierEntity.supplierEntity
import com.toyproject.babmuckja.supplier.entities.SupplierEntity
import org.springframework.stereotype.Repository

@Repository
class QSupplierRepository(
    val query: JPAQueryFactory
) {
    //tests
    fun getAllSupplierName(): List<String> {
        return query.select(supplierEntity.supplierName)
            .from(supplierEntity)
            .limit(10)
            .fetch()
    }

    fun getSupplierByIdInEager(supplierId: Long): Any? {
        return query.from(supplierEntity)
            .leftJoin(QProductEntity.productEntity)
            .fetchJoin()
            .where(supplierEntity.id.eq(supplierId))
            .fetchOne()
    }

}