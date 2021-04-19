package com.toyproject.babmuckja.user

import com.toyproject.babmuckja.user.entities.UserEntity
import com.toyproject.babmuckja.user.repositories.QUserRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
    private val qUserRepository: QUserRepository
) {

    @GetMapping("/{id}")
    @Throws(Exception::class)
    fun getUserById(@PathVariable id: Long): UserEntity {
        return userService.getUserById(id)
    }

    @GetMapping("/all")
    @Throws(Exception::class)
    fun getAllUser(): List<String> {
        return qUserRepository.getUsersAll()
    }
}