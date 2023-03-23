package com.many.affection.user.entity

import jakarta.persistence.*

@Entity
class Tag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(unique = true)
    var name: String
) : BaseEntity() {
    @OneToMany(mappedBy = "tag")
    var postTagList: MutableList<PostTag>? = null
}