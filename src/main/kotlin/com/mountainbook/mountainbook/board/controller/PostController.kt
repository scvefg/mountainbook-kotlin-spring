package com.mountainbook.mountainbook.board.controller

import com.mountainbook.mountainbook.board.dto.PostDto
import com.mountainbook.mountainbook.board.service.PostService
import com.mountainbook.mountainbook.config.CustomUserDetails
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

@RequestMapping("/posts")
@RestController
class PostController(
    var postService: PostService
) {

    @PostMapping
    fun write(@RequestBody post: PostDto.Request): ResponseEntity<*> {
        return ResponseEntity.ok(postService.write(getUsername(),post))
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody post: PostDto.Request): ResponseEntity<*> {
        return ResponseEntity.ok(postService.update(id, getUsername(), post))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity.ok(postService.delete(id, getUsername()))
    }

    @GetMapping("/{id}")
    fun getPost(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity.ok(postService.getPost(id))
    }

    @GetMapping("/hits")
    fun getPost(): ResponseEntity<*> {
        return ResponseEntity.ok(postService.postOrderByHits())
    }

    fun getUsername(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        val userDetails = authentication.principal as CustomUserDetails
        return userDetails.username
    }


}