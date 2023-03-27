package com.mountainbook.mountainbook.user.controller

import com.mountainbook.mountainbook.config.CustomUserDetails
import com.mountainbook.mountainbook.user.dto.ImageDto
import com.mountainbook.mountainbook.user.service.ImageService
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping("/image")
@RestController
class ImageController(
    var imageService: ImageService
) {


    @PutMapping
    fun saveProfileImage(@RequestBody image: ImageDto.ProfileImageDto): ResponseEntity<*> {
        return ResponseEntity.ok(imageService.save(image, getUsername()))
    }

    @GetMapping("/{userId}")
    fun getProfileImage(@PathVariable userId: UUID): ResponseEntity<*> {
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(imageService.getProfileImage(userId))
    }

    fun getUsername(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        val userDetails = authentication.principal as CustomUserDetails
        return userDetails.username
    }
}