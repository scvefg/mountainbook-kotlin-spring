package com.mountainbook.mountainbook.user.repository

import com.mountainbook.mountainbook.user.entity.ProfileImage
import com.mountainbook.mountainbook.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface ImageRepository: JpaRepository<ProfileImage, Long> {

    fun findByUser(user: User):ProfileImage?
}