package com.many.affection.board.entity

import com.many.affection.user.entity.BaseEntity
import jakarta.persistence.*

@Entity
class PostTag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    var post: Post?,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    @JoinColumn(name = "tag_id")
    var tag: Tag?
) : BaseEntity() {


}