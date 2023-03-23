package com.many.affection.board.repository

import com.many.affection.board.entity.Post
import com.many.affection.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long>{
    fun findAllByUser(user: User) : Post?
    fun findAllByOrderByHitsDesc() : MutableList<Post>?
}
