package com.triangl.dashboard.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class WebConfig: WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PATCH", "DELETE", "OPTIONS")
            .allowCredentials(true)
            .allowedHeaders("Authorization", "Content-Type")
            .maxAge(3600)
        super.addCorsMappings(registry)
    }
}