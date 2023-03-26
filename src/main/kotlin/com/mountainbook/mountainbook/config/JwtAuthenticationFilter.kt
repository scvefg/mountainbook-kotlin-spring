package com.mountainbook.mountainbook.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    var tokenProvider: TokenProvider
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        var token: String? = request.getHeader("Authorization")

        if (token != null && token.startsWith("Bearer ")) {
            var jwt = token.substring(7)
            if (tokenProvider.validToken(jwt)) {
                try {
                    jwt = token.substring(7)
                    SecurityContextHolder.getContext().authentication = tokenProvider.getAuthentication(jwt)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
        filterChain.doFilter(request, response)
    }
}

