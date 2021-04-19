package com.toyproject.babmuckja.billing

import com.toyproject.babmuckja.billing.constants.BillState
import com.toyproject.babmuckja.billing.constants.PaymentMethod
import com.toyproject.babmuckja.billing.dtos.*
import com.toyproject.babmuckja.billing.entities.BillingEntity
import com.toyproject.babmuckja.billing.repositories.BillingRepository
import com.toyproject.babmuckja.billing.repositories.QBillingRepository
import com.toyproject.babmuckja.order.OrderDetailService
import com.toyproject.babmuckja.order.OrderService
import com.toyproject.babmuckja.order.constants.OrderState
import com.toyproject.babmuckja.order.entities.OrderEntity
import com.toyproject.babmuckja.order.entities.QOrderDetailEntity
import com.toyproject.babmuckja.product.entities.QProductEntity
import com.toyproject.babmuckja.supplier.entities.QSupplierEntity
import com.toyproject.babmuckja.user.UserService
import javassist.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Service
class BillingService(
    val userService: UserService,
    val orderDetailService: OrderDetailService,
    val billingRepository: BillingRepository,
    val qBillingRepository: QBillingRepository,
    val orderService: OrderService,
) {
    fun completePayment(createBillingDto: CreateBillingDto): BillingEntity {
        val user = userService.getUserByEmail(createBillingDto.email)
        user ?: throw NotFoundException("user not found")
        var priceSum: Int = 0
        val quentitiesAndPrices =
            orderDetailService.getQuentitiesAndPricesByOrderIds(createBillingDto.orderIds)
        println(" size : $quentitiesAndPrices")

        quentitiesAndPrices.forEach {
            val quentity = it.get(QOrderDetailEntity.orderDetailEntity.quentity)
            val price = it.get(QProductEntity.productEntity.price)
            if (quentity == null || price == null)
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Input Validation Failed")
            println("$quentity : $price")
            priceSum += quentity * price
        }

        // Todo : validation

        // validation 끝나면 저장.
        val orders = orderService.getOrdersByIds(createBillingDto.orderIds)

        var bill = BillingEntity(
            user = user,
            orders = orders,
            price = priceSum,
            paymentMethod = createBillingDto.paymentMethod,
            billState = BillState.COMPLETE,
            deadline = LocalDateTime.now().plusDays(7)
        )
        val billResult = billingRepository.save(bill)
        orders.forEach {
            it.orderState = OrderState.COMPLETION
        }
        orderService.saveOrders(orders)
        return billResult
    }

    fun addPending(addPendingDto: AddPendingDto) : String {
        val user = userService.getUserByEmail(addPendingDto.email)
        user?: throw NotFoundException("user not found")
        val quentitiesAndPrices =
            orderDetailService.getQuentitiesAndPricesByOrderIds(addPendingDto.orderIds)
        println(" size : $quentitiesAndPrices")

        var priceSum: Int = 0
        quentitiesAndPrices.forEach {
            val quentity = it.get(QOrderDetailEntity.orderDetailEntity.quentity)
            val price = it.get(QProductEntity.productEntity.price)
            if (quentity == null || price == null)
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Input Validation Failed")
            println("$quentity : $price")
            priceSum += quentity * price
        }
        val orders = orderService.getOrdersByIds(addPendingDto.orderIds)
        orders.forEach {
            it.orderState = OrderState.PENDING
        }
        var bill = BillingEntity(
            user = user,
            orders = orders,
            price = priceSum,
            paymentMethod = PaymentMethod.PENDING,
            billState = BillState.PENDING,
            deadline= LocalDateTime.now().plusDays(7)
        )
        billingRepository.save(bill)
        orderService.saveOrders(orders)

        return "add pending success"
    }

    fun paymentPendingBilling(paymentPendingBillingDto: PaymentPendingBillingDto) : BillingEntity {
        var billing = billingRepository.findById(paymentPendingBillingDto.billId).get()
        billing.billState = BillState.COMPLETE
        billing.paymentMethod = paymentPendingBillingDto.paymentMethod
        return billingRepository.save(billing)
    }
    fun getPendingBill(email : String) : List<GetPendingBillResultDto> {
        val user = userService.getUserByEmail(email)
        user?: throw NotFoundException("user not found")
        val pendingBills = qBillingRepository.getPendingBillsByUserId(user.id!!)
        val returnList : MutableList<GetPendingBillResultDto> = ArrayList()

        pendingBills.forEach {
            val orderList : MutableList<Long> = ArrayList()
            it.orders.forEach { order->
                orderList.add(order.id!!)
            }
            returnList.add(
                GetPendingBillResultDto(
                    billId = it.id!!,
                    price = it.price,
                    createAt = it.createAt,
                    deadline = it.deadline
                )
            )
        }
        return returnList;
    }

    fun getOrderIdsByBillId(billId: Long) : BillingEntity {
        return billingRepository.findById(billId).get()
    }

    fun getMyBillings(getMyBillingDto: GetMyBillingDto) : List<GetMyBillingResultDto> {
        val userId = userService.getUserIdByEmail(getMyBillingDto.email)
        val billings = qBillingRepository.getBillingByUserId(userId)
        var getMyBillingDtoList :MutableList<GetMyBillingResultDto> = ArrayList()

        billings.forEach { billing->
            if(billing.billState ==BillState.CANCLE)
                return@forEach
            println(billing)
            var orderIds : MutableList<Long> = ArrayList()
            billing.orders.forEach { order->
                orderIds.add(order.id!!)
            }
            println(orderIds)
            val orderInfo = orderDetailService.getBillingOrderDetailInfoByOrderIds(orderIds)
            var infos : MutableList<BillingOrderDetailInfo> = ArrayList()
            orderInfo.forEach {
                infos.add(
                    BillingOrderDetailInfo(
                        productName=it.get(QProductEntity.productEntity.productName)!!,
                        quentity=it.get(QOrderDetailEntity.orderDetailEntity.quentity)!!,
                        price = it.get(QProductEntity.productEntity.price)!!,
                        supplierName = it.get(QSupplierEntity.supplierEntity.supplierName)!!,
                        supplierId = it.get(QSupplierEntity.supplierEntity.id)!!
                    )
                )
            }
            getMyBillingDtoList.add(
                GetMyBillingResultDto(
                    billState = billing.billState,
                    createAt = billing.createAt,
                    paymentMethod =billing.paymentMethod,
                    billingPrice = billing.price,
                    orderDetails = infos
                )
            )
        }

        return getMyBillingDtoList
    }

    fun getBillingById(getBillDetailDto: GetBillDetailDto) : BillingEntity {
        val billing = billingRepository.findById(getBillDetailDto.billId).get()
        return billing
    }


    fun cancleBilling(cancleBillingDto: CancleBillingDto)  {
        val bill = billingRepository.findById(cancleBillingDto.billId).get()
        bill.billState = BillState.CANCLE
        billingRepository.save(bill)
    }
}