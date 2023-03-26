package com.mountainbook.mountainbook.config

import com.mountainbook.mountainbook.user.service.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    var userRepository: UserRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username) ?: throw RuntimeException("User not found!")
        return CustomUserDetails(user)
    }
}