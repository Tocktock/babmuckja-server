package com.toyproject.babmuckja.billing.entities

import com.toyproject.babmuckja.billing.constants.BillState
import com.toyproject.babmuckja.billing.constants.PaymentMethod
import com.toyproject.babmuckja.order.entities.OrderEntity
import com.toyproject.babmuckja.user.entities.UserEntity
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "billings")
@EntityListeners(AuditingEntityListener::class)
data class BillingEntity(
    @OneToOne(
        orphanRemoval = true,
    )
    val user: UserEntity,
    @ManyToMany(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST, CascadeType.REMOVE]
    )
    val orders: MutableList<OrderEntity>,
    val price: Int,
    var paymentMethod: PaymentMethod,
    var billState : BillState,
    var deadline : LocalDateTime
) {
    @Id
    @SequenceGenerator(name = "billing_sequence", sequenceName = "billing_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "billing_sequence")
    var id: Long? = null

    @CreatedDate
    lateinit var createAt: LocalDateTime

    //pending : 결제 만료일, not pending : 취소 가능일?
}