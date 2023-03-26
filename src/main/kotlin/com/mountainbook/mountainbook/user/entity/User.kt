package com.mountainbook.mountainbook.user.entity

import com.mountainbook.mountainbook.board.entity.Post
import com.mountainbook.mountainbook.group.entity.Group
import com.mountainbook.mountainbook.user.type.UserStatus
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import java.time.LocalDate
import java.util.*

@Entity
class User(
    @Id
    @GeneratedValue(generator = "uuid")
    @Column(name = "user_id")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    var id: UUID? = null,
    var username: String,
    var password: String,
    var nickname: String,
    var email: String,
    var birth: LocalDate,

    @Enumerated(EnumType.STRING) // specify that the enum should be saved as a string
    var status: UserStatus

) : BaseEntity() {
    @OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE])
    lateinit var postList: MutableList<Post>

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    var group: Group? = null
}