package com.toyproject.babmuckja.order

import com.toyproject.babmuckja.order.dtos.*
import com.toyproject.babmuckja.order.entities.OrderDetailEntity
import com.toyproject.babmuckja.order.entities.OrderEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/order")
class OrderController(
    val orderService: OrderService,
    val orderDetailService: OrderDetailService
) {

    @GetMapping("/{email}")
    @Throws(Exception::class)
    fun getOrdersByEmail(@PathVariable(name = "email") email: String) {
    }

    @PostMapping("/add/basket")
    @Throws(Exception::class)
    fun addBasket(
        @RequestBody @Valid addBasketDto: AddBasketDto,
    ) : String {
        orderService.addBasket(addBasketDto)
        return "add basket success"
    }

//    @PostMapping("/pending")
//    @Throws(Exception::class)
//    fun pendingRequest(@RequestBody pendingOrderRequestDto: PendingOrderRequestDto): String {
//        return orderService.pendingOrders(pendingOrderRequestDto)
//    }

    @PostMapping("/mybasket")
    @Throws(Exception::class)
    fun getMyBasket (@RequestBody getMyBasketDto : GetMyBasketDto) : List<BasketItemDto>{
        return orderService.getMyBasket(getMyBasketDto)
    }

    @PostMapping("/pending")
    @Throws(Exception::class)
    fun getPendingOrders(@RequestBody  pendingOrderRequestDto: PendingOrderRequestDto) :MutableList<GetOrderInfoListDto> {
        return orderService.getPendingOrders(pendingOrderRequestDto)
    }

    @PostMapping("/mybasket/change-order-detail")
    @Throws(Exception::class)
    fun changeOrderDetail(@RequestBody changeOrderDetailDto: ChangeOrderDetailDto) : String {
      return  orderDetailService.updateOrderDetail(changeOrderDetailDto)
    }

} 