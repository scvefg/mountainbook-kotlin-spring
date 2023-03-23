package com.many.affection.group.repository

import com.many.affection.group.entity.Group
import org.springframework.data.jpa.repository.JpaRepository

interface GroupRepository: JpaRepository<Group, Long> {
}
