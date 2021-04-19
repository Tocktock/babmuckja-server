package com.toyproject.babmuckja.user.entities

import com.toyproject.babmuckja.user.enums.AppUserRole
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity(name = "users")
@Table(
    name = "users",
    indexes = [
        Index(name = "users_index_email", columnList = "email", unique = true)
    ]
)
data class UserEntity(
    @Column(unique = true)
    @NotNull
    var email: String = "",
    @NotNull
    private var password: String = "",
    @NotNull
    private var username: String = ""
) : UserDetails {

    constructor(id: Long) : this() {
        this.id = id
    }

    @Id
    @SequenceGenerator(name = "users_sequence", sequenceName = "users_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_sequence")
    var id: Long? = null;


    @Enumerated(EnumType.STRING)
    private var appUserRole: AppUserRole = AppUserRole.USER

    private var enabled: Boolean = true;

    private var locked: Boolean = false;

    private var refreshToken: String = "";

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        var list = ArrayList<GrantedAuthority>()
        list.add(SimpleGrantedAuthority(appUserRole.name))
        return list
    }

    override fun getPassword(): String {
        return password
    }

    //email will validate user
    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return !locked
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    //because username declared with private val, setUsername is needed
    fun setUsername(username: String) {
        this.username = username
    }

    //because password declared with private val, setPassword is needed
    fun setPassword(password: String) {
        this.password = password
    }

    override fun toString(): String {
        return "UserEntity(id=$id, email='$email', password='$password', username='$username', appUserRole=$appUserRole, enabled=$enabled, locked=$locked)"
    }


}