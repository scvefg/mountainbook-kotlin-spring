package com.many.affection.board.repository

import com.many.affection.board.entity.Post
import com.many.affection.board.entity.PostTag
import org.springframework.data.jpa.repository.JpaRepository

interface PostTagRepository : JpaRepository<PostTag, Long> {

    fun findAllByPost(post: Post): MutableList<PostTag>?
}
