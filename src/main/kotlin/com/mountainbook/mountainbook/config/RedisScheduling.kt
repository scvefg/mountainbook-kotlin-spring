package com.mountainbook.mountainbook.config

import com.mountainbook.mountainbook.board.repository.PostRepository
import mu.KotlinLogging
import org.springframework.data.redis.core.RedisTemplate
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

        allPost.map { x ->
            if (x.id != null) {
                val key = "${x.id}"
                x.views = operations.get(key) ?: 1L
            }
        }

        postRepository.saveAll(allPost)
    }


    @Scheduled(fixedDelay = 86400000)
    fun removeCache() {
        redisTemplate.delete("postView")
    }
}
