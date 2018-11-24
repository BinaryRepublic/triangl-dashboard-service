package com.triangl.dashboard.config

import javax.servlet.http.HttpServletResponse
import java.io.IOException
import javax.servlet.*

class CorsFilter : Filter {

    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig) {

    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        val response = servletResponse as HttpServletResponse

        response.setHeader("Access-Control-Allow-Origin", "*")
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT,OPTIONS")
        response.setHeader("Access-Control-Allow-Headers", "*")
        response.setHeader("Access-Control-Allow-Credentials", "true")
        response.setHeader("Access-Control-Max-Age", "180")
        filterChain.doFilter(servletRequest, servletResponse)
    }

    override fun destroy() {

    }
}