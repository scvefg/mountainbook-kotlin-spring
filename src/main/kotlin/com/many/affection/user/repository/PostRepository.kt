package com.many.affection.user.repository

import com.many.affection.user.entity.Post
import com.many.affection.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long>{
    fun findAllByUser(user: User) : Post?
}
