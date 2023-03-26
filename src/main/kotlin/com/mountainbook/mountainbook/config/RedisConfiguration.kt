package com.mountainbook.mountainbook.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Suppress("DEPRECATION")
@Configuration
class RedisConfiguration {

    @Value("\${spring.data.redis.host}")
    private var host: String = ""

    @Value("\${spring.data.redis.port}")
    private var port: Int = 0

    /**
     * LettuceConnectionFactory를 사용하여 Lettuce로 Redis와의 연결을 진행한다.
     * RedisTemplate는 Redis의 커넥션 위에서 데이터의 추가, 삭제, 조회를 위해서 사용
     * StringRedisTemplate는 문자열에 특화된 메서드를 제공 -> 직렬화 or 역직렬화를 할 때 좀 더 편하게 사용 가능
     */

    @Bean
    fun lettuceConnectionFactory(): LettuceConnectionFactory {
        return LettuceConnectionFactory(RedisStandaloneConfiguration(host, port))
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, Long> {
        var template = RedisTemplate<String, Long>()

        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = Jackson2JsonRedisSerializer<Long>(Long::class.java)
        template.setConnectionFactory(lettuceConnectionFactory())

        return template
    }

    @Bean
    fun stringRedisTemplate(redisConnectionFactory: RedisConnectionFactory): StringRedisTemplate {
        var template = StringRedisTemplate()
        template.setConnectionFactory(lettuceConnectionFactory())
        return template
    }
}