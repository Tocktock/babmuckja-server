package com.toyproject.babmuckja.billing.repositories

import com.querydsl.jpa.impl.JPAQueryFactory
import com.toyproject.babmuckja.billing.constants.BillState
import com.toyproject.babmuckja.billing.entities.BillingEntity
import com.toyproject.babmuckja.billing.entities.QBillingEntity
import com.toyproject.babmuckja.billing.entities.QBillingEntity.billingEntity
import com.toyproject.babmuckja.order.entities.OrderEntity
import com.toyproject.babmuckja.order.entities.QOrderEntity
import com.toyproject.babmuckja.user.entities.QUserEntity
import org.springframework.stereotype.Repository

@Repository
class QBillingRepository(
    val query: JPAQueryFactory
) {

    fun getPendingBillsByUserId(userId : Long) : List<BillingEntity> {
        return query.select(billingEntity)
            .from(billingEntity)
            .where(billingEntity.billState.eq(BillState.PENDING).and(billingEntity.user.id.eq(userId)))
            .fetch()
    }
    fun getOrderIdsByBillingId(billId : Long) : BillingEntity{
        return query.select(billingEntity)
            .from(billingEntity)
            .where(billingEntity.id.eq(billId))
            .fetchOne()!!
    }

    fun getBillingByUserId(userId : Long) : List<BillingEntity> {
        return query.select(billingEntity)
            .from(billingEntity)
            .where(billingEntity.user.id.eq(userId))
            .fetch()
    }

}