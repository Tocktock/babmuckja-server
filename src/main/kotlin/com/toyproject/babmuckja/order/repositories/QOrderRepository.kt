package com.toyproject.babmuckja.order.repositories

import com.querydsl.core.Tuple
import com.querydsl.jpa.impl.JPAQueryFactory
import com.toyproject.babmuckja.billing.entities.QBillingEntity
import com.toyproject.babmuckja.order.constants.OrderState
import com.toyproject.babmuckja.order.entities.OrderEntity
import com.toyproject.babmuckja.order.entities.QOrderEntity.orderEntity
import com.toyproject.babmuckja.supplier.entities.QSupplierEntity
import com.toyproject.babmuckja.supplier.entities.QSupplierEntity.supplierEntity
import org.springframework.stereotype.Repository

@Repository
class QOrderRepository(
    val query: JPAQueryFactory,
) {

    fun getOrders(): List<Long> {
        return query.select(orderEntity.id).from(orderEntity).fetch()
    }

    fun getOrderIdsWithStatusBasketByUserId(userId: Long) : List<Long> {
        return query.select(orderEntity.id)
            .from(orderEntity)
            .where(orderEntity.user.id.eq(userId).and(orderEntity.orderState.eq(OrderState.BASKET)))
            .fetch()
    }

    fun getOrderWithSupplierByUserId(userId : Long) : List<Tuple> {
        return query.select(orderEntity.id, supplierEntity.id ,supplierEntity.supplierName)
            .from(orderEntity)
            .leftJoin(supplierEntity)
            .on(supplierEntity.id.eq(orderEntity.supplier.id))
            .where(orderEntity.user.id.eq(userId).and(orderEntity.orderState.eq(OrderState.BASKET)))
            .fetch()
    }

    fun getOrderItemWithSupplierByOrderIds(orderIds : List<Long>) : List<Tuple> {
        return query.select(orderEntity.id, supplierEntity.id ,supplierEntity.supplierName)
            .from(orderEntity)
            .leftJoin(supplierEntity)
            .on(supplierEntity.id.eq(orderEntity.supplier.id))
            .where(orderEntity.id.`in`(orderIds))
            .fetch()
    }

    fun orderedSupplier(suppliedId: Long, userId: Long): OrderEntity? {
        return query.select(orderEntity)
            .from(orderEntity)
            .where(orderEntity.user.id.eq(userId).and(orderEntity.supplier.id.eq(suppliedId)))
            .fetchOne()
    }
//    fun orderExist(productId: Long, userId: Long): Long? {
//        val result = query.select(orderDetailEntity.id)
//            .from(orderDetailEntity)
//            .leftJoin(orderEntity, orderEntity)
//            .on(orderEntity.id.eq(orderDetailEntity.id))
//            .where(orderEntity.user.id.eq(userId).and(orderDetailEntity.product.id.eq(productId)))
//            .fetchOne()
//        return result ?: null
//    }
}