package com.mountainbook.mountainbook.board.service

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@DataJpaTest
class PostServiceTest(
    @Autowired
    var redisTemplate: RedisTemplate<String, Long>,
    var postService: PostService
) {

    @BeforeEach
    fun setup() {

    }

    @AfterEach
    fun tearDown() {
    }


    @Test
    fun redisTest() {
    }
}