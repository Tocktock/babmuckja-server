package com.toyproject.babmuckja.order.repositories

import com.querydsl.core.Tuple
import com.querydsl.jpa.impl.JPAQueryFactory
import com.toyproject.babmuckja.order.entities.OrderDetailEntity
import com.toyproject.babmuckja.order.entities.QOrderDetailEntity.orderDetailEntity
import com.toyproject.babmuckja.order.entities.QOrderEntity.orderEntity
import com.toyproject.babmuckja.product.entities.QProductEntity.productEntity
import com.toyproject.babmuckja.supplier.entities.QSupplierEntity
import com.toyproject.babmuckja.supplier.entities.QSupplierEntity.supplierEntity
import org.springframework.stereotype.Repository

@Repository
class QOrderDetailRepository(
    val query: JPAQueryFactory,
) {

    fun getOrderDetailsByOrderId(orderId : Long) : List<OrderDetailEntity> {
        return query.select(orderDetailEntity)
            .from(orderDetailEntity)
            .where(orderDetailEntity.order.id.eq(orderId))
            .fetch()
    }

    fun getBillingOrderDetailInfoByOrderIds(orderIds : List<Long>) : List<Tuple> {
        return query.select(
            productEntity.productName, orderDetailEntity.quentity, productEntity.price
        , supplierEntity.supplierName, supplierEntity.id)
            .from(orderDetailEntity)
            .leftJoin(productEntity)
            .on(productEntity.id.eq(orderDetailEntity.product.id))
            .leftJoin(supplierEntity)
            .on(supplierEntity.id.eq(productEntity.supplier.id))
            .where(orderDetailEntity.order.id.`in`(orderIds))
            .fetch()
    }

    fun getOrderDetailAndProductByOrderId(orderId: Long) : List<Tuple> {
        val list = query.select(
            orderDetailEntity.quentity,
            productEntity.id,
            productEntity.productName,
            productEntity.price)
            .from(orderDetailEntity)
            .leftJoin(productEntity)
            .on(productEntity.id.eq(orderDetailEntity.product.id))
            .where(orderDetailEntity.order.id.eq(orderId))
            .fetch()
        return list
    }

    fun getOrderDetailWithStatusBasketByOrderIds(orderIds : List<Long>) : List<Tuple> {
        val list = query.select(orderDetailEntity.quentity, productEntity.id,
            productEntity.productName, productEntity.price
            , supplierEntity.supplierName, supplierEntity.id)
            .from(orderDetailEntity)
            .leftJoin(productEntity)
            .on(productEntity.id.eq(orderDetailEntity.product.id))
            .leftJoin(supplierEntity)
            .on(supplierEntity.id.eq(productEntity.id))
            .where(orderDetailEntity.order.id.`in`(orderIds))
            .fetch()
        return list
    }


    fun getOrderDetailByOrderId(orderId: Long): Map<Long?, OrderDetailEntity> {
        val list = query.select(orderDetailEntity)
            .from(orderDetailEntity)
            .leftJoin(orderEntity)
            .on(orderEntity.id.eq(orderDetailEntity.order.id))
            .where(orderEntity.id.eq(orderId))
            .fetch().map { it.product.id to it }.toMap()
        return list
    }

    fun getOrderDetailQuentitesAndProductPricesByOrderIds(orderIds: List<Long>)
            : MutableList<Tuple> {
        val list = query.select(orderDetailEntity.quentity, productEntity.price)
            .from(productEntity)
            .leftJoin(orderDetailEntity)
            .on(productEntity.id.eq(orderDetailEntity.product.id))
            .leftJoin(orderEntity)
            .on(orderEntity.id.eq(orderDetailEntity.order.id))
            .where(orderEntity.id.`in`(orderIds))
            .fetch()
        return list
    }
}