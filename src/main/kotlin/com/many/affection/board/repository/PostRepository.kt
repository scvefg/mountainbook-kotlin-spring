package com.many.affection.board.repository

import com.many.affection.board.entity.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long>{
    fun findAllByOrderByViewsDesc() : MutableList<Post>?
}
