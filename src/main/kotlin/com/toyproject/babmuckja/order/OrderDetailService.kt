package com.toyproject.babmuckja.order

import com.querydsl.core.Tuple
import com.toyproject.babmuckja.order.dtos.ChangeOrderDetailDto
import com.toyproject.babmuckja.order.entities.OrderDetailEntity
import com.toyproject.babmuckja.order.repositories.OrderDetailRepository
import com.toyproject.babmuckja.order.repositories.OrderRepository
import com.toyproject.babmuckja.order.repositories.QOrderDetailRepository
import javassist.NotFoundException
import org.springframework.stereotype.Service

@Service
class OrderDetailService(
    val orderDetailRepository: OrderDetailRepository,
    val qOrderDetailRepository: QOrderDetailRepository,
    val orderRepository : OrderRepository
) {

    fun getQuentitiesAndPricesByOrderIds(orderIds: List<Long>): MutableList<Tuple> {
        val list = qOrderDetailRepository
            .getOrderDetailQuentitesAndProductPricesByOrderIds(orderIds)
        return list
    }

    fun getBillingOrderDetailInfoByOrderIds(orderIds : List<Long>) : List<Tuple> {
        return qOrderDetailRepository.getBillingOrderDetailInfoByOrderIds(orderIds)
    }

    fun updateOrderDetail(changeOrderDetailDto: ChangeOrderDetailDto) : String {
        println("update orderdetail init")
        val orderDetails = qOrderDetailRepository.getOrderDetailByOrderId(orderId = changeOrderDetailDto.orderId)
        println(orderDetails)
        lateinit var targetOrderDetail : OrderDetailEntity
        orderDetails.forEach {
            if(it.value.product.id == changeOrderDetailDto.productId) {
                targetOrderDetail = it.value
            }
        }
        targetOrderDetail.quentity = changeOrderDetailDto.quentity
        orderDetailRepository.save(targetOrderDetail)
        return "update successfully done"
    }
}