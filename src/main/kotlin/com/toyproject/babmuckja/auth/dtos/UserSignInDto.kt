package com.toyproject.babmuckja.auth.dtos

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

class UserSignInDto() {
    @Email
    @NotEmpty
    val email: String? = null

    @NotEmpty
    val password: String? = null
}