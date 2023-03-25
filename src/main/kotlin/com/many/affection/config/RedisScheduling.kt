package com.many.affection.config

import com.many.affection.board.repository.PostRepository
import mu.KotlinLogging
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class RedisScheduling(
    var postRepository: PostRepository,
    var redisTemplate: RedisTemplate<String, Long>
) {

    var log = KotlinLogging.logger() {}

    @Scheduled(fixedDelay = 60000)
    fun allPostSaveViews() {
        val operations: ValueOperations<String, Long> = redisTemplate.opsForValue()


        val allPost = postRepository.findAll()

        for (post in allPost) {
            if (post.id != null) {
                val key = "${post.id}"
                post.views = operations.get(key)?:1L
                postRepository.save(post)

            }
        }


    }


    @Scheduled(fixedDelay = 86400000)
    fun removeCache() {
        redisTemplate.delete("postView")
    }
}
