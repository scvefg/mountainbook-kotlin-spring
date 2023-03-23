package com.many.affection.user.entity

import jakarta.persistence.*

@Entity
class Post(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    var contents: String,
    var title: String,
    var hits: Long,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User

) : BaseEntity() {

    @OneToMany(mappedBy = "post", cascade = [CascadeType.REMOVE])
    var postTagList: MutableList<PostTag>? = mutableListOf()
}