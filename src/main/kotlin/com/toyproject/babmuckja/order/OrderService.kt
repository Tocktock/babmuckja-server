package com.toyproject.babmuckja.order

import com.querydsl.core.types.Order
import com.toyproject.babmuckja.billing.BillingService
import com.toyproject.babmuckja.order.constants.OrderState
import com.toyproject.babmuckja.order.dtos.*
import com.toyproject.babmuckja.order.entities.OrderDetailEntity
import com.toyproject.babmuckja.order.entities.OrderEntity
import com.toyproject.babmuckja.order.entities.QOrderDetailEntity
import com.toyproject.babmuckja.order.entities.QOrderEntity
import com.toyproject.babmuckja.order.repositories.OrderDetailRepository
import com.toyproject.babmuckja.order.repositories.OrderRepository
import com.toyproject.babmuckja.order.repositories.QOrderDetailRepository
import com.toyproject.babmuckja.order.repositories.QOrderRepository
import com.toyproject.babmuckja.product.ProductService
import com.toyproject.babmuckja.product.entities.ProductEntity
import com.toyproject.babmuckja.product.entities.QProductEntity
import com.toyproject.babmuckja.supplier.entities.QSupplierEntity
import com.toyproject.babmuckja.supplier.entities.SupplierEntity
import com.toyproject.babmuckja.user.UserService
import com.toyproject.babmuckja.user.entities.UserEntity
import javassist.NotFoundException
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class OrderService(
    val orderRepository: OrderRepository,
    val orderDetailRepository: OrderDetailRepository,
    val qOrderRepository: QOrderRepository,
    val qOrderDetailRepository: QOrderDetailRepository,
    val userService: UserService,
    val productService: ProductService,
    @Lazy val billingService: BillingService
) {

    fun getMyBasket(getMyBasketDto: GetMyBasketDto) : List<BasketItemDto> {
        val userId = userService.getUserIdByEmail(getMyBasketDto.email)
        val orderWithSupplier = qOrderRepository.getOrderWithSupplierByUserId(userId)
        val basketDto : MutableList<BasketItemDto> = ArrayList()

        orderWithSupplier.forEach {
            val items = qOrderDetailRepository
                .getOrderDetailAndProductByOrderId(it.get(QOrderEntity.orderEntity.id)!!)
            val productList : MutableList<BasketItemInfo> = ArrayList()
            items.forEach { tuple->
                productList.add(BasketItemInfo(
                    tuple.get(QOrderDetailEntity.orderDetailEntity.quentity)!!,
                    tuple.get(QProductEntity.productEntity.id)!!,
                    tuple.get(QProductEntity.productEntity.productName)!!,
                    tuple.get(QProductEntity.productEntity.price)!!
                ))
            }
            basketDto.add(BasketItemDto(
                orderId = it.get(QOrderEntity.orderEntity.id)!!,
                supplierId = it.get(QSupplierEntity.supplierEntity.id)!!,
                supplierName = it.get(QSupplierEntity.supplierEntity.supplierName)!!,
                items =  productList
            ))
        }
        return basketDto
//        val tuples  = qOrderDetailRepository.getOrderDetailWithStatusBasketByOrderIds(orders)
//        val basketItem : MutableList<BasketItemInfo> = ArrayList()
//        val result : MutableList<BasketItemDto> = ArrayList()
//        tuples.forEach {
//            basketItem.add(BasketItemInfo(
//                     it.get(QOrderDetailEntity.orderDetailEntity.quentity)!!,
//                            it.get( QProductEntity.productEntity.id)!!,
//                            it.get( QProductEntity.productEntity.productName)!!,
//                            it.get( QProductEntity.productEntity.price)!!
//                ))
//
//        }
//        // order- supplier 정보 가져오고,
//        // detail- product 정보 가져오기.
////        it.get(QSupplierEntity.supplierEntity.id)!!
////        it.get(QSupplierEntity.supplierEntity.supplierName)!!
//
//
//        return basketItem
    }

    fun getOrdersByIds(orderIds: List<Long>): MutableList<OrderEntity> {
        return orderRepository.findAllById(orderIds)
    }

    fun saveOrder(order: OrderEntity) {
        orderRepository.save(order)
    }

    fun saveOrders(orders: List<OrderEntity>) {
        orderRepository.saveAll(orders)
    }

    fun addBasket(addBasketDto: AddBasketDto) {
        //input validation
        if (!productService.validateProduct(addBasketDto)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "supplier does not have that product")
        }
        // productid가 없으면 생성.
        val userId = userService.getUserIdByEmail(addBasketDto.email)

        //Todo : basket 상태의 order 들을 찾아야함.
        val orderedSupplier = qOrderRepository.orderedSupplier(addBasketDto.supplierId, userId)

        if (orderedSupplier != null) {
            updateBasket(addBasketDto, orderedSupplier)
            return
        }

        val order = orderRepository.save(
            OrderEntity(
                orderState = OrderState.BASKET,
                user = UserEntity(userId),
                supplier = SupplierEntity(addBasketDto.supplierId)
            )
        )

        var orderDetailList: MutableList<OrderDetailEntity> = ArrayList<OrderDetailEntity>()
        addBasketDto.orderDetailInfo.forEach {
            orderDetailList.add(
                OrderDetailEntity(
                    order = order,
                    product = ProductEntity(it.productId),
                    quentity = it.quentity
                )
            )
        }
        orderDetailRepository.saveAll(orderDetailList)
    }

    fun updateBasket(addBasketDto: AddBasketDto, order: OrderEntity) {
        //order 존재 여부는 체크했음.
        var orderDetailMap = qOrderDetailRepository.getOrderDetailByOrderId(order.id!!)
        var targetOrderDetailList = ArrayList<OrderDetailEntity>()
        //없는데 있는 아이는 0으로 만들어줄 필요가 있음.
        var tempList : MutableList<Long> = ArrayList()
        addBasketDto.orderDetailInfo.forEach {
            tempList.add(it.productId)
        }

        orderDetailMap.forEach { map->
            if(!tempList.contains(map.value.id)) {
                map.value.quentity = 0
                targetOrderDetailList.add(map.value)
            }
        }

        addBasketDto.orderDetailInfo.forEach {
            var orderDetail = orderDetailMap.get(it.productId)
            if (orderDetail != null) {

                orderDetail.quentity = it.quentity
                targetOrderDetailList.add(orderDetail)
            } else {
                //같은 가게인데 order detail에 메뉴가 존재하지 않는다면
                targetOrderDetailList.add(
                    OrderDetailEntity(
                        order, ProductEntity(it.productId), it.quentity
                    )
                )
            }
        }
        order.orderState=OrderState.BASKET
        if (targetOrderDetailList.size > 0) {
            orderDetailRepository.saveAll(targetOrderDetailList)
        }
    }


    fun getPendingOrders(pendingOrderRequestDto: PendingOrderRequestDto) : MutableList<GetOrderInfoListDto> {
        val orders = billingService.getOrderIdsByBillId(pendingOrderRequestDto.billId).orders
        val orderIds: MutableList<Long> = ArrayList()
        orders.forEach {
            orderIds.add(it.id!!)
        }
        val orderWithSupplier = qOrderRepository.getOrderItemWithSupplierByOrderIds(orderIds)
        val orderInfoListDto : MutableList<GetOrderInfoListDto> = ArrayList()

        orderWithSupplier.forEach {
            val items = qOrderDetailRepository
                .getOrderDetailAndProductByOrderId(it.get(QOrderEntity.orderEntity.id)!!)
            val productList : MutableList<OrderItemInfo> = ArrayList()
            items.forEach { tuple->
                productList.add(
                    OrderItemInfo(
                    tuple.get(QOrderDetailEntity.orderDetailEntity.quentity)!!,
                    tuple.get(QProductEntity.productEntity.id)!!,
                    tuple.get(QProductEntity.productEntity.productName)!!,
                    tuple.get(QProductEntity.productEntity.price)!!
                )
                )
            }
            orderInfoListDto.add(GetOrderInfoListDto(
                orderId = it.get(QOrderEntity.orderEntity.id)!!,
                supplierId = it.get(QSupplierEntity.supplierEntity.id)!!,
                supplierName = it.get(QSupplierEntity.supplierEntity.supplierName)!!,
                items =  productList
            ))
        }
        return orderInfoListDto
    }
}