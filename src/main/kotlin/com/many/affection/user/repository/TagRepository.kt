package com.many.affection.user.repository

import com.many.affection.user.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository: JpaRepository<Tag, Long> {

    fun findByName(name: String): Tag?
}