package com.toyproject.babmuckja.billing

import com.toyproject.babmuckja.billing.dtos.*
import com.toyproject.babmuckja.billing.entities.BillingEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/billing")
class BillingController(
    val billingService: BillingService
) {

    @PostMapping("/complete")
    @Throws(Exception::class)
    fun completePayment(@RequestBody createBillingDto: CreateBillingDto): BillingEntity {
        return billingService.completePayment(createBillingDto)
    }

    @PostMapping("/pending/add")
    @Throws(Exception::class)
    fun addPending(@RequestBody addPendingDto: AddPendingDto ) : String {
        return billingService.addPending(addPendingDto)
    }

    @PostMapping("/pending/get")
    @Throws(Exception::class)
    fun getPendingBill(@RequestBody getPendingBillDto: GetPendingBillDto) : List<GetPendingBillResultDto> {
        return billingService.getPendingBill(getPendingBillDto.email)
    }

    @PostMapping("/pending/complete")
    @Throws(Exception::class)
    fun completePendingBill(@RequestBody paymentPendingBillingDto: PaymentPendingBillingDto) : BillingEntity {
        return billingService.paymentPendingBilling(paymentPendingBillingDto)
    }

    @PostMapping("/mybilling")
    @Throws(Exception::class)
    fun getMyBilling(@RequestBody getMyBillingDto: GetMyBillingDto) : List<GetMyBillingResultDto> {
       return  billingService.getMyBillings(getMyBillingDto)
    }

    @PostMapping("/cancle")
    @Throws(Exception::class)
    fun cancleBilling(@RequestBody cancleBillingDto: CancleBillingDto) : String {
        billingService.cancleBilling(cancleBillingDto)
        return "cancle success"
    }

    @PostMapping("/detail")
    @Throws(Exception::class)
    fun getBillingDetail(@RequestBody getBillDetailDto: GetBillDetailDto) : BillingEntity{
        return billingService.getBillingById(getBillDetailDto)
    }
}