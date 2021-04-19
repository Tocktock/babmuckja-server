package com.toyproject.babmuckja.supplier.repositories

import com.toyproject.babmuckja.supplier.constants.SupplierCategory
import com.toyproject.babmuckja.supplier.entities.SupplierEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SupplierRepository : JpaRepository<SupplierEntity, Long> {
    fun findByCategory(supplierCategory: SupplierCategory, pageable: Pageable?): Page<SupplierEntity?>?
}