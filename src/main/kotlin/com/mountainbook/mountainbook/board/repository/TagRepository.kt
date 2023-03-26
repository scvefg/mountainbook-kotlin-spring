package com.mountainbook.mountainbook.board.repository

import com.mountainbook.mountainbook.board.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository: JpaRepository<Tag, Long> {

    fun findByName(name: String): Tag?
}