package com.toyproject.babmuckja

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class BabmuckjaApplication

fun main(args: Array<String>) {
    runApplication<BabmuckjaApplication>(*args)
}
