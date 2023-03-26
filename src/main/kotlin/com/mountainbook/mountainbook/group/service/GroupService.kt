package com.mountainbook.mountainbook.group.service

import com.mountainbook.mountainbook.group.dto.GroupDto
import com.mountainbook.mountainbook.group.entity.Group
import com.mountainbook.mountainbook.group.repository.GroupRepository
import com.mountainbook.mountainbook.user.service.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.format.DateTimeFormatter

@Service
class GroupService(
    var groupRepository: GroupRepository,
    var userRepository: UserRepository
) {
    @Transactional
    fun recruiting(username: String, request: GroupDto.RequestDto) {
        val user = userRepository.findByUsername(username) ?: throw RuntimeException("User not found!")
        var userList = mutableListOf(user)
        var group = Group(
            fee = request.fee,
            totalMemberNum = request.total,
            recruitingPeriod = request.period,
            place = request.place
        )

        group.userList = userList
        group.leaderUsername = user.username

        user.group = groupRepository.save(group)
    }

    @Transactional
    fun update(id: Long, username: String, request: GroupDto.RequestDto) {
        val user = userRepository.findByUsername(username) ?: throw RuntimeException("User not found!")
        val findGroup = groupRepository.findById(id).orElseThrow { throw RuntimeException("Group not found!") }

        if (user.username != findGroup.leaderUsername) {
            throw RuntimeException("Can modify only leader!")
        }

        user.group?.fee = request.fee
        user.group?.totalMemberNum = request.total
        user.group?.recruitingPeriod = request.period
        user.group?.place = request.place
    }

    @Transactional
    fun joinGroup(id: Long, username: String) {
        val user = userRepository.findByUsername(username) ?: throw RuntimeException("User not found!")
        val findGroup = groupRepository.findById(id).orElseThrow { throw RuntimeException("Group not found!") }

        var result = findGroup.userList.find { it.username.equals(user.username) }
        if (result != null) {
            throw RuntimeException("Already Join")
        }

        user.group = findGroup
        findGroup.userList.add(user)
    }

    @Transactional
    fun exitGroup(id: Long, username: String) {
        val user = userRepository.findByUsername(username) ?: throw RuntimeException("User not found!")
        val findGroup = groupRepository.findById(id).orElseThrow { throw RuntimeException("Group not found!") }

        var result = findGroup.userList.find { it.username.equals(user.username) }
        if (result == null) {
            throw RuntimeException("Not found you in Group!")
        }

        user.group = null
        findGroup.userList.remove(user)
    }

    //TODO: Pagination 필요
    fun getGroupList(): MutableList<GroupDto.ResponseDto> {
        val allGroup = groupRepository.findAll()
        var groupList = mutableListOf<GroupDto.ResponseDto>()
        for (group in allGroup) {
            val userList = group.userList
            val usernameList = mutableListOf<String>()
            for (user in userList) {
                usernameList.add(user.username)
            }

            groupList.add(
                GroupDto.ResponseDto(
                    fee = group.fee,
                    total = group.totalMemberNum,
                    current = group.currentMemberNum,
                    period = group.recruitingPeriod.format(DateTimeFormatter.ofPattern("YYYY-MM-dd")),
                    place = group.place,
                    leader = group.leaderUsername,
                    memberList = usernameList
                )
            )
        }
        return groupList
    }
}