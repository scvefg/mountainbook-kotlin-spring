package com.mountainbook.mountainbook.group.controller

import com.mountainbook.mountainbook.config.CustomUserDetails
import com.mountainbook.mountainbook.group.dto.GroupDto
import com.mountainbook.mountainbook.group.service.GroupService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/groups")
@RestController
class GroupController(
    var groupService: GroupService
) {

    @PostMapping
    fun recruiting(@RequestBody request: GroupDto.RequestDto): ResponseEntity<*>{
        return ResponseEntity.ok(groupService.recruiting(getUsername(), request))
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: GroupDto.RequestDto ): ResponseEntity<*>{
        return ResponseEntity.ok(groupService.update(id = id, username = getUsername(), request = request))
    }

    @PostMapping("/{id}")
    fun join(@PathVariable id: Long): ResponseEntity<*>{
        return ResponseEntity.ok(groupService.joinGroup(id = id, username = getUsername()))
    }
    @PostMapping("/{id}/exit")
    fun exit(@PathVariable id: Long): ResponseEntity<*>{
        return ResponseEntity.ok(groupService.exitGroup(id = id, username = getUsername()))
    }
    @GetMapping
    fun getAllGroup(): ResponseEntity<*>{
        return ResponseEntity.ok(groupService.getGroupList())
    }
    fun getUsername(): String{
        val authentication = SecurityContextHolder.getContext().authentication
        val userDetails = authentication.principal as CustomUserDetails
        return userDetails.username
    }
}