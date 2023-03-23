package com.many.affection.user.repository

import com.many.affection.user.entity.Post
import com.many.affection.user.entity.PostTag
import com.many.affection.user.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository

interface PostTagRepository : JpaRepository<PostTag, Long> {

    fun findAllByPostAndTag(post: Post, tag: Tag): MutableList<PostTag>?
}
