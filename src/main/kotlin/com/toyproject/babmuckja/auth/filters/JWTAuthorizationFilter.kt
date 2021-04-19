package com.toyproject.babmuckja.auth.filters

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.toyproject.babmuckja.auth.constants.SecurityConstants
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.io.IOException
import javax.naming.AuthenticationException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthorizationFilter(authenticatinnManager: AuthenticationManager) :
    BasicAuthenticationFilter(authenticatinnManager) {

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
        val header = req.getHeader(SecurityConstants.HEADER_STRING)
        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(req, res)
            return
        }
        val authentication = getAuthetication(req)
        SecurityContextHolder.getContext().authentication = authentication

        // Todo : refresh token management.
        chain.doFilter(req, res)
    }

    private fun getAuthetication(req: HttpServletRequest): UsernamePasswordAuthenticationToken {
        val token = req.getHeader(SecurityConstants.HEADER_STRING).replace(SecurityConstants.TOKEN_PREFIX, "")
        val user = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.toByteArray()))
            .build().verify(token).subject
            ?: throw AuthenticationException("not authorized user")
        return UsernamePasswordAuthenticationToken(user, null, ArrayList())
    }
}