package com.triangl.dashboard.support

import com.triangl.dashboard.repository.servingDB.CustomerRepository
import io.jsonwebtoken.Jwts
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import javax.annotation.PostConstruct
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.*
import javax.servlet.http.HttpServletRequest
import java.security.spec.X509EncodedKeySpec
import java.security.KeyFactory
import java.security.PublicKey
import java.util.Base64.getDecoder

@Component
class JwtTokenProvider (
    private val customerRepository: CustomerRepository
) {
    private val restTemplate = RestTemplate()
    lateinit var secretKey: String

    @PostConstruct
    protected fun init() {
        try {
            val res = restTemplate.exchange(
                "https://api.triangl.io/auth/.well-known/jwks.json",
                HttpMethod.GET,
                null,
                String::class.java
            )
            this.secretKey = res.body!!
                .replace("\n", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
        } catch (e: Exception) {
            throw IllegalArgumentException("could not fetch public key for JWT signature verification")
        }
    }

    fun getAuthentication(token: String): Authentication {
        val customerId = getUserId(token)
        val customer = customerRepository.findByUserId(customerId)
            ?: throw NoCustomerAssignedToUserException("No customer was found with id $customerId")
        return UsernamePasswordAuthenticationToken(customer, "", emptyList())
    }

    fun getUserId(token: String): String {
        return Jwts.parser().setSigningKey(getParsedPubKey()).parseClaimsJws(token).body["user_id"] as String
    }

    fun resolveToken(req: HttpServletRequest): String? {
        val bearerToken = req.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7, bearerToken.length)
        } else null
    }

    fun getParsedPubKey(): PublicKey {
        val publicKeyDER = getDecoder().decode(secretKey)
        val keySpec = X509EncodedKeySpec(publicKeyDER)

        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(keySpec)
    }

    fun validateToken(token: String): Boolean {
        try {
            val claims = Jwts.parser().setSigningKey(getParsedPubKey()).parseClaimsJws(token)
            return !claims.body.expiration.before(Date())
        } catch (e: Exception) {
            throw InvalidJwtAuthenticationException("Expired or invalid JWT token")
        } catch (e: IllegalArgumentException) {
            throw InvalidJwtAuthenticationException("Expired or invalid JWT token")
        }
    }
}