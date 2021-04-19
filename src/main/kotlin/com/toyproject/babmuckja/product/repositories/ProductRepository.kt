package com.toyproject.babmuckja.product.repositories

import com.toyproject.babmuckja.product.entities.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<ProductEntity, Long> {
}