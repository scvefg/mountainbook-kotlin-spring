package com.many.affection.user.controller

import com.many.affection.config.TokenProvider
import com.many.affection.user.dto.TokenDto
import com.many.affection.user.dto.UserDto
import com.many.affection.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/user/auth")
@RestController
class AuthController(
    var authenticationManager: AuthenticationManager,
    var tokenProvider: TokenProvider,
    var userService: UserService
) {

    @PostMapping
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
}