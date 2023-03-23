package com.many.affection.user.entity

import com.many.affection.board.entity.Post
import com.many.affection.group.entity.Group
import jakarta.persistence.*

@Entity
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    var id: Long? = null,
    var username: String,
    var password: String,
) : BaseEntity() {
    @OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE])
    lateinit var postList: MutableList<Post>

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    var group: Group? = null
}