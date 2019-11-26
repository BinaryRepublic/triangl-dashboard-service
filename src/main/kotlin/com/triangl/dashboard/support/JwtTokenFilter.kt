package com.triangl.dashboard.support

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.io.IOException
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtTokenFilter(private val jwtTokenProvider: JwtTokenProvider) : GenericFilterBean() {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(req: ServletRequest, res: ServletResponse, filterChain: FilterChain) {
        try {
            val token = jwtTokenProvider.resolveToken(req as HttpServletRequest)
            if (token != null && jwtTokenProvider.validateToken(token)) {
                val auth = jwtTokenProvider.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = auth
            }
            filterChain.doFilter(req, res)
        } catch (e: InvalidJwtAuthenticationException) {
            (res as HttpServletResponse).status = 401
            res.writer.write(e.message!!)
            res.writer.flush()
            res.writer.close()
        } catch (e: NoCustomerAssignedToUserException) {
            (res as HttpServletResponse).status = 401
            res.writer.write(e.message!!)
            res.writer.flush()
            res.writer.close()
        }
    }
}