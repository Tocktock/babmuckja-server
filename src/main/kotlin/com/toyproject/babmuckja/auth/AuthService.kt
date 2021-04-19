package com.toyproject.babmuckja.auth

import com.toyproject.babmuckja.auth.dtos.UserSignInResultDto
import com.toyproject.babmuckja.user.UserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthService (
    val userService: UserService
        ){

    fun signInWithToken() : UserSignInResultDto {
        val context = SecurityContextHolder.getContext()
        val authentication = context.authentication
        val tokenEmail = authentication.name
        val user = userService.getUserByEmail(tokenEmail)
        println(user)
        return UserSignInResultDto(user?.email, user?.username,"")
    }
}