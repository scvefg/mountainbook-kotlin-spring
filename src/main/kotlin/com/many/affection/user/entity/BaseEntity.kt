package com.many.affection.user.entity

import java.time.LocalDateTime

abstract class BaseEntity(
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
)