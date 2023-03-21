package com.many.affection.user.repository

import com.many.affection.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long>{
    fun findByUsername(username: String): User?
}
