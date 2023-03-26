package com.mountainbook.mountainbook.board.repository

import com.mountainbook.mountainbook.board.entity.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long>{
    fun findAllByOrderByViewsDesc() : MutableList<Post>?
}
