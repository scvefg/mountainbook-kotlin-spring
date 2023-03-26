package com.mountainbook.mountainbook.user.controller

import com.mountainbook.mountainbook.config.TokenProvider
import com.mountainbook.mountainbook.user.dto.TokenDto
import com.mountainbook.mountainbook.user.dto.UserDto
import com.mountainbook.mountainbook.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/auth")
@RestController
class AuthController(
    var authenticationManager: AuthenticationManager,
    var tokenProvider: TokenProvider,
    var userService: UserService
) {

    @PostMapping("/login")
    fun login(@RequestBody loginDto: UserDto): ResponseEntity<*> {
        userService.login(loginDto)
        var authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginDto.username,
                loginDto.password
            )
        )
        SecurityContextHolder.getContext().authentication = authentication
        return ResponseEntity.ok(TokenDto(tokenProvider.createToken(loginDto.username)))
    }

    @PostMapping("/logout")
    fun logout(@RequestBody token: String): ResponseEntity<*>{
        return ResponseEntity.ok(tokenProvider.setTokenExpirationPastValue(token))
    }
}