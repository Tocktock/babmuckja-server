package com.toyproject.babmuckja.auth

import com.toyproject.babmuckja.auth.dtos.RegisterUserDto
import com.toyproject.babmuckja.auth.dtos.UserSignInDto
import com.toyproject.babmuckja.auth.dtos.UserSignInResultDto
import com.toyproject.babmuckja.user.UserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/auth")
class AuthController(
    val authService: AuthService,
    val userService: UserService
) {

    @PostMapping("/user/sign-up")
    @Throws(Exception::class)
    fun registerUser(@RequestBody @Valid registerUserDto: RegisterUserDto): String {
        val user = userService.registerUser(registerUserDto)
        return user.username
    }

    @PostMapping("/user/sign-in")
    fun authenticateUser(@RequestBody @Valid userSignInDto: UserSignInDto) {
        // JWTAuthenticationfilter will authenticate user.
    }

    @PostMapping("/user/accessToken")
    fun authenticateUserWithHeadterToken() : UserSignInResultDto {
       return authService.signInWithToken()
    }


}