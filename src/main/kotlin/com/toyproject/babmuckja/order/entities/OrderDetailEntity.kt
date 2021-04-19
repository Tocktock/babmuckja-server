package com.toyproject.babmuckja.order.entities

import com.toyproject.babmuckja.product.entities.ProductEntity
import javax.persistence.*

@Entity
@Table(name = "order_detail")
data class OrderDetailEntity(
    @ManyToOne(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST]
    )
    val order: OrderEntity,
    @ManyToOne(cascade = [CascadeType.DETACH])
    val product: ProductEntity,
    var quentity: Int
) {
    @Id
    @SequenceGenerator(name = "order_detail_sequence", sequenceName = "order_detail_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_detail_sequence")
    var id: Long? = null
}