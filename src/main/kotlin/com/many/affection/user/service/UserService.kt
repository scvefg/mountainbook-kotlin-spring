package com.many.affection.user.service

import com.many.affection.user.dto.UserDto
import com.many.affection.user.entity.User
import com.many.affection.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    var userRepository: UserRepository,
    var passwordEncoder: PasswordEncoder
) {
    fun signUp(signUpDto: UserDto) {
        val findUser = userRepository.findByUsername(signUpDto.username)
        findUser?.let { throw RuntimeException("Duplicated user!") }
        userRepository.save(
            User(
                username = signUpDto.username,
                password = passwordEncoder.encode(signUpDto.password)
            )
        )
    }

    fun login(loginDto: UserDto) {
        var user = userRepository.findByUsername(loginDto.username) ?: throw RuntimeException("User not found!")
        if (!passwordEncoder.matches(loginDto.password, user.password)) throw RuntimeException("Not equal password!")
    }

    @Transactional
    fun delete(username: String) {
        val findUser = userRepository.findByUsername(username)?: throw RuntimeException("User not found!")
        userRepository.delete(findUser)
    }
}
