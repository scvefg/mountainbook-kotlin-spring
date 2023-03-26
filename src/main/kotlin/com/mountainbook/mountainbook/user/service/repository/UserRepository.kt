package com.mountainbook.mountainbook.user.service.repository

import com.mountainbook.mountainbook.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository: JpaRepository<User, UUID>{
    fun findByUsername(username: String): User?
}
