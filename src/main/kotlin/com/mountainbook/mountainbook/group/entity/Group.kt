package com.mountainbook.mountainbook.group.entity

import com.mountainbook.mountainbook.user.entity.User
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "Groups")
class Group(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var fee: Long,
    var totalMemberNum: Long,
    var recruitingPeriod: LocalDate,
    var place: String
){
    var leaderUsername: String = ""
    var currentMemberNum: Long = 0L

    @OneToMany(mappedBy = "group" ,fetch = FetchType.LAZY)
    var userList: MutableList<User> = mutableListOf()
}