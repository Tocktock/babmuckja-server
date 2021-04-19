package com.toyproject.babmuckja.product.entities

import com.toyproject.babmuckja.supplier.entities.SupplierEntity
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "products")
data class ProductEntity(
    @NotNull
    var productName: String = "",
    @NotNull
    var price: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    private val supplier: SupplierEntity,
) {

    constructor(id: Long) : this(
        "", 0, SupplierEntity(0)
    ) {
        this.id = id
    }


    @Id
    @SequenceGenerator(name = "products_sequence", sequenceName = "products_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_sequence")
    var id: Long? = null

}