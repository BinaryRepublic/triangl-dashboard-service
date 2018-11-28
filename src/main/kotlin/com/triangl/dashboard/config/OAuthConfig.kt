package com.triangl.dashboard.config

import com.auth0.spring.security.api.JwtWebSecurityConfigurer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
@Configuration
@Profile("production")
class OAuthConfig : WebSecurityConfigurerAdapter() {

    @Value(value = "\${auth0.apiAudience}")
    private val apiAudience: String? = null
    @Value(value = "\${auth0.issuer}")
    private val issuer: String? = null

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        JwtWebSecurityConfigurer
            .forRS256(apiAudience, issuer)
            .configure(http)
            .authorizeRequests()
            .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
            .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
            .antMatchers("/**").hasAuthority("read:stats")
            .anyRequest().denyAll()
    }
}

@EnableWebSecurity
@Configuration
@Profile("test")
class OAuthConfigTest : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
            .anyRequest().permitAll()
            .and().csrf().disable()
    }
}