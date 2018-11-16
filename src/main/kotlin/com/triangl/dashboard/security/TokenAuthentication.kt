package com.triangl.dashboard.security

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.GrantedAuthority
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.security.authentication.AbstractAuthenticationToken
import java.util.*

private fun readAuthorities(jwt: DecodedJWT): Collection<GrantedAuthority> {
    val rolesClaim = jwt.getClaim("https://access.control/roles")
    if (rolesClaim.isNull) {
        return Collections.emptyList()
    }
    val authorities = ArrayList<GrantedAuthority>()
    val scopes = rolesClaim.asArray(String::class.java)
    for (s in scopes) {
        val a = SimpleGrantedAuthority(s)
        if (!authorities.contains(a)) {
            authorities.add(a)
        }
    }
    return authorities
}

class TokenAuthentication(private val jwt: DecodedJWT) : AbstractAuthenticationToken(readAuthorities(jwt)) {
    private var invalidated: Boolean = false

    private fun hasExpired(): Boolean {
        return jwt.expiresAt.before(Date())
    }

    override fun getCredentials(): String {
        return jwt.token
    }

    override fun getPrincipal(): Any {
        return jwt.subject
    }

    override fun setAuthenticated(authenticated: Boolean) {
        if (authenticated) {
            throw IllegalArgumentException("Create a new Authentication object to authenticate")
        }
        invalidated = true
    }

    override fun isAuthenticated(): Boolean {
        return !invalidated && !hasExpired()
    }
}