package com.toyproject.babmuckja.auth.filters

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.ObjectMapper
import com.toyproject.babmuckja.auth.constants.SecurityConstants
import com.toyproject.babmuckja.auth.dtos.UserSignInDto
import com.toyproject.babmuckja.auth.dtos.UserSignInResultDto
import com.toyproject.babmuckja.user.entities.UserEntity
import org.springframework.http.ResponseCookie
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import java.lang.System.currentTimeMillis
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.ArrayList

class JWTAuthenticationFilter(authenticatinnManager: AuthenticationManager) :
    UsernamePasswordAuthenticationFilter(authenticatinnManager) {

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(req: HttpServletRequest, res: HttpServletResponse): Authentication {
        try {
            val userSignInDto = ObjectMapper().readValue(req.inputStream, UserSignInDto::class.java)
            return authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    userSignInDto.email, userSignInDto.password, ArrayList()
                )
            )
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun successfulAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        println(authResult)
        val token = JWT.create().withSubject((authResult.principal as UserEntity).email)
            .withExpiresAt(Date(currentTimeMillis() + 1000 * 60 *60*24))
            .sign(Algorithm.HMAC512(SecurityConstants.SECRET.toByteArray()))
        val userSignInResultDto = UserSignInResultDto(
            email = (authResult.principal as UserEntity).email,
            username = (authResult.principal as UserEntity).username,
            accessToken = token)
        val mapper = ObjectMapper()
        val toJson = mapper.writeValueAsString(userSignInResultDto)

        // Todo : Cookie 에 token 작성.

//
//        val responseCookie = ResponseCookie.from("Authentication",token)
//            .httpOnly(true)
//            .path("/")
//            .maxAge(86400)
//            .build()
//        //secure 옵션은 https 에서만 작동.
//        res.setHeader("Set-Cookie", responseCookie.toString())
        res.writer.write(toJson)
        res.writer.flush()
    }

    init {
        setFilterProcessesUrl("/auth/user/sign-in")
    }
}