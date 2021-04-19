package com.toyproject.babmuckja.billing.repositories

import com.toyproject.babmuckja.billing.entities.BillingEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BillingRepository : JpaRepository<BillingEntity, Long> {
}