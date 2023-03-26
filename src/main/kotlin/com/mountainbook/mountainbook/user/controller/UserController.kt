package com.mountainbook.mountainbook.user.controller

import com.mountainbook.mountainbook.config.CustomUserDetails
import com.mountainbook.mountainbook.user.dto.MailDto
import com.mountainbook.mountainbook.user.dto.UserDto
import com.mountainbook.mountainbook.user.service.MailService
import com.mountainbook.mountainbook.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RequestMapping("/user")
@RestController
class UserController(
    var userService: UserService,
    var mailService: MailService,

    ) {
    @PostMapping
    fun createUser(@RequestBody signUpDto: UserDto): ResponseEntity<*> {
        return ResponseEntity.ok(userService.signUp(signUpDto))
    }

    @DeleteMapping
    fun deleteUser(): ResponseEntity<*> {
        return ResponseEntity.ok(userService.delete(getUsername()))
    }


    @GetMapping("/mail/{userId}")
    fun verifyMail(@PathVariable userId: UUID): ResponseEntity<*> {
        return ResponseEntity.ok(userService.verifyMail(userId))
    }

    fun getUsername(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        val userDetails = authentication.principal as CustomUserDetails
        return userDetails.username
    }
}