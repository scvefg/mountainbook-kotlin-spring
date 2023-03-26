package com.mountainbook.mountainbook.group.repository

import com.mountainbook.mountainbook.group.entity.Group
import org.springframework.data.jpa.repository.JpaRepository

interface GroupRepository: JpaRepository<Group, Long> {
}
