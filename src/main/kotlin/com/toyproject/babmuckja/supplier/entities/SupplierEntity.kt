package com.toyproject.babmuckja.supplier.entities

import com.toyproject.babmuckja.product.entities.ProductEntity
import com.toyproject.babmuckja.supplier.constants.SupplierCategory
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(
    name = "suppliers",
    indexes = [
        Index(name = "supplier_index_category", columnList = "category", unique = false)
    ]
)
data class SupplierEntity(
    @NotNull
    var supplierName: String = "",
    @NotNull
    @Enumerated(EnumType.STRING)
    var category: SupplierCategory = SupplierCategory.WESTERN,
    @NotNull
    var contactInfo: String,
    @NotNull
    var location: String = "",
    @NotNull
    @Column(precision = 3, scale = 7)
    var latitude: Double = 0.0,
    @NotNull
    @Column(precision = 3, scale = 7)
    var longitude: Double = 0.0,

    ) {

    constructor(id: Long) : this(
        "", SupplierCategory.WESTERN, "", "", 0.0, 0.0
    ) {
        this.id = id
    }

    @Id
    @SequenceGenerator(name = "suppliers_sequence", sequenceName = "suppliers_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "suppliers_sequence")
    var id: Long? = null

    @OneToMany(
        fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.REMOVE]
    )
    @OrderBy("product_name asc")
    var products: MutableList<ProductEntity> = ArrayList()
}