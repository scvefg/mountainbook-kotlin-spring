package com.mountainbook.mountainbook.user.service

import com.mountainbook.mountainbook.user.dto.MailDto
import com.mountainbook.mountainbook.user.entity.User
import com.mountainbook.mountainbook.user.service.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service


@Service
class MailService(
    var mailSender: JavaMailSender,
    var userRepository: UserRepository,
    @Value("\${mail.url}")
    var url: String = "",
    @Value("\${mail.title}")
    var title: String = "",
) {

    fun sendMail(mailDto: MailDto) {

        var user: User = userRepository.findByUsername(mailDto.username) ?: throw RuntimeException("User not found!");

        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)

        helper.setTo(mailDto.address)
        helper.setSubject(title)
        helper.setText("${url}${user.id}")

        mailSender.send(message)
    }

}