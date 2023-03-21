package com.many.affection.user.controller

import com.many.affection.config.CustomUserDetails
import com.many.affection.user.dto.UserDto
import com.many.affection.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RequestMapping("/user")
@RestController
class UserController(
    var userService: UserService
) {
    @PostMapping
    fun createUser(@RequestBody signUpDto: UserDto): ResponseEntity<*> {
        return ResponseEntity.ok(userService.signUp(signUpDto))
    }

    @DeleteMapping
    fun deleteUser(): ResponseEntity<*> {
        return ResponseEntity.ok(userService.delete(getUsername()))
    }


    fun getUsername(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        val userDetails = authentication.principal as CustomUserDetails
        return userDetails.username
    }
}