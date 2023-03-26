package com.mountainbook.mountainbook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class MountainbookApplication

fun main(args: Array<String>) {
    runApplication<MountainbookApplication>(*args)
}
