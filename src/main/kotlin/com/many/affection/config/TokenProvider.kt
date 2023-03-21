package com.many.affection.config

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Suppress("DEPRECATION")
@Component
class TokenProvider(
    var userDetailsService: CustomUserDetailsService
) {
    var log = KotlinLogging.logger {}


    @Value("\${jwt.secret}")
    var secret: String = ""

    @Value("\${jwt.expiration}")
    var expiration: Long = 0L


    fun createToken(username: String): String {
        var now = Date(System.currentTimeMillis())
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + expiration))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val username = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).body.subject
        val userDetails = userDetailsService.loadUserByUsername(username)
        return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
    }

    fun validToken(token: String): Boolean {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
            return true

        } catch (ex: Exception) {
            return false
        }
    }

    fun getUsernameFromToken(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(secret)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }
}
