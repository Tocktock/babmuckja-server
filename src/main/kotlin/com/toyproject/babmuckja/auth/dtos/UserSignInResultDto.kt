package com.toyproject.babmuckja.auth.dtos

data class UserSignInResultDto(
    val email : String? = null,
    val username: String? = null,
    val accessToken: String? = null
) {
}