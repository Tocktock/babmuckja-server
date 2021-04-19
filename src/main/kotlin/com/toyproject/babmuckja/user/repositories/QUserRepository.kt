package com.toyproject.babmuckja.user.repositories

import com.querydsl.jpa.impl.JPAQueryFactory
import com.toyproject.babmuckja.user.entities.QUserEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Repository

@Repository
class QUserRepository(
    val query: JPAQueryFactory
) {
    fun getUsersAll(): List<String> {
        return query.select(QUserEntity.userEntity.email)
            .from(QUserEntity.userEntity).fetch()
    }

    fun getUserIdByEmail(email: String): Long {
        val userId = query.select(QUserEntity.userEntity.id)
            .from(QUserEntity.userEntity)
            .where(QUserEntity.userEntity.email.eq(email))
            .fetchOne()
        userId ?: throw UsernameNotFoundException("invalid email")
        return userId
    }
}