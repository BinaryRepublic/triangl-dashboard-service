package com.triangl.dashboard.dbModels.servingDB.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
@Table(name = "Customer")
open class Customer : UserDetails {

    @Id
    var id: String? = null

    var name: String? = null

    @OneToMany(mappedBy = "customerId")
    var maps: Set<Map>? = null

    var lastUpdatedAt: String? = null

    var createdAt: String? = null

    var userId: String? = null

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return emptyList()
    }

    override fun getPassword(): String? {
        return null
    }

    override fun getUsername(): String? {
        return null
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}