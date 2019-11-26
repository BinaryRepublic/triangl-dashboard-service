package com.triangl.dashboard.config

import com.triangl.dashboard.support.JwtTokenFilter
import com.triangl.dashboard.support.JwtTokenProvider
import org.springframework.context.annotation.Configuration
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.config.annotation.SecurityConfigurerAdapter

@Configuration
class JwtConfigurer(private val jwtTokenProvider: JwtTokenProvider) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        val customFilter = JwtTokenFilter(jwtTokenProvider)
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}