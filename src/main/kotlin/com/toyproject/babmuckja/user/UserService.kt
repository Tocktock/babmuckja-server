package com.toyproject.babmuckja.user

import com.toyproject.babmuckja.auth.dtos.RegisterUserDto
import com.toyproject.babmuckja.user.entities.UserEntity
import com.toyproject.babmuckja.user.repositories.QUserRepository
import com.toyproject.babmuckja.user.repositories.UserRepository
import javassist.NotFoundException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val qUserRepository: QUserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) : UserDetailsService {

    fun getUserById(id: Long): UserEntity {
        val user = userRepository.findById(id)
        return user.get()
    }

    fun getUserIdByEmail(email: String): Long {
        return qUserRepository.getUserIdByEmail(email)
    }

    fun getUserByEmail(email: String): UserEntity? {
        return userRepository.findOneByEmail(email)
    }

    //email is validator.
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails {
        return userRepository.findOneByEmail(email) ?: throw UsernameNotFoundException(
            "$email does not exist"
        )
    }

    fun registerUser(registerUserDto: RegisterUserDto): UserEntity {
        val encodedPassword = bCryptPasswordEncoder.encode(registerUserDto.password)
        val newUser = UserEntity()
        newUser.email = registerUserDto.email!!
        newUser.username = registerUserDto.username!!
        newUser.password = encodedPassword
        return userRepository.save(newUser)
    }


}