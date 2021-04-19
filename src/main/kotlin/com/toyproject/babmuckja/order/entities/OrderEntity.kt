package com.toyproject.babmuckja.order.entities

import com.toyproject.babmuckja.order.constants.OrderState
import com.toyproject.babmuckja.supplier.entities.SupplierEntity
import com.toyproject.babmuckja.user.entities.UserEntity
import javax.persistence.*

@Entity
@Table(name = "orders")
data class OrderEntity(
    var orderState: OrderState,

    @ManyToOne(
        fetch = FetchType.LAZY,
    )
    private val user: UserEntity,
    // Todo : change manytoone and management
    @OneToOne(
        orphanRemoval = true,
    )
    val supplier: SupplierEntity
) {
    constructor(orderId: Long) : this(
        orderState = OrderState.BASKET,
        user = UserEntity(0),
        supplier = SupplierEntity(0)
    ) {
        this.id = orderId
    }

    @Id
    @SequenceGenerator(name = "orders_sequence", sequenceName = "orders_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_sequence")
    var id: Long? = null
}