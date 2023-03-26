package com.mountainbook.mountainbook.user.service

import com.mountainbook.mountainbook.user.dto.MailDto
import com.mountainbook.mountainbook.user.dto.UserDto
import com.mountainbook.mountainbook.user.entity.User
import com.mountainbook.mountainbook.user.service.repository.UserRepository
import com.mountainbook.mountainbook.user.type.UserStatus
import mu.KotlinLogging
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UserService(
    var userRepository: UserRepository,
    var passwordEncoder: PasswordEncoder,
    var mailService: MailService
) {

    var log = KotlinLogging.logger(){}

    @Transactional
    fun signUp(userDto: UserDto) {
        val findUser = userRepository.findByUsername(userDto.username)
        findUser?.let { throw RuntimeException("Duplicated user!") }
        var savedUser = userRepository.save(
            User(
                username = userDto.username,
                password = passwordEncoder.encode(userDto.password),
                nickname = userDto.nickname,
                email = userDto.email,
                birth = userDto.birth,
                status = UserStatus.UNACTIVE
            )
        )

        var mailDto = MailDto(
            address = savedUser.email,
            username = savedUser.username
        )

        mailService.sendMail(mailDto)
        log.info("${savedUser.username} join !")

    }

    fun login(loginDto: UserDto) {
        var user = userRepository.findByUsername(loginDto.username) ?: throw RuntimeException("User not found!")
        if (!passwordEncoder.matches(loginDto.password, user.password)) throw RuntimeException("Not equal password!")
    }

    @Transactional
    fun delete(username: String) {
        val findUser = userRepository.findByUsername(username) ?: throw RuntimeException("User not found!")
        userRepository.delete(findUser)
    }

    @Transactional
    fun verifyMail(userId: UUID) {
        val user = userRepository.findById(userId).orElseThrow { throw RuntimeException("User not found!") }
        if (user.status == UserStatus.UNACTIVE) {
            user.status = UserStatus.ACTIVE
        }
    }


}
