package com.many.affection.board.service

import com.many.affection.board.dto.PostDto
import com.many.affection.board.repository.PostRepository
import com.many.affection.config.RedisConfiguration
import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
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