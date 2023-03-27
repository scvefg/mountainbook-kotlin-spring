package com.mountainbook.mountainbook.user.entity

import jakarta.persistence.*

@Entity
class ProfileImage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var src: String,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User
)