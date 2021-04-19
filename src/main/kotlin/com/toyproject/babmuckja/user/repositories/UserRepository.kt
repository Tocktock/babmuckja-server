package com.toyproject.babmuckja.user.repositories

import com.toyproject.babmuckja.user.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findOneByEmail(email: String): UserEntity?

}