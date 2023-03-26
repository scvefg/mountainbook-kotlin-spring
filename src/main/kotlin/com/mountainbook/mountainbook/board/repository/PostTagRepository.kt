package com.mountainbook.mountainbook.board.repository

import com.mountainbook.mountainbook.board.entity.Post
import com.mountainbook.mountainbook.board.entity.PostTag
import org.springframework.data.jpa.repository.JpaRepository

interface PostTagRepository : JpaRepository<PostTag, Long> {

    fun findAllByPost(post: Post): MutableList<PostTag>?
}
