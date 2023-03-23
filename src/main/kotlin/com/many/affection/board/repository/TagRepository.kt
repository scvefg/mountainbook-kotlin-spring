package com.many.affection.board.repository

import com.many.affection.board.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository: JpaRepository<Tag, Long> {

    fun findByName(name: String): Tag?
}