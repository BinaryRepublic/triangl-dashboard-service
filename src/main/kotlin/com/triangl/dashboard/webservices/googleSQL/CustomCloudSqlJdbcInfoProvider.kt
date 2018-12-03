package com.triangl.dashboard.webservices.googleSQL

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gcp.autoconfigure.sql.CloudSqlJdbcInfoProvider
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("production")
@Configuration
class CustomCloudSqlJdbcInfoProvider: CloudSqlJdbcInfoProvider {

    @Value("\${spring.cloud.gcp.sql.jdbc-url}")
    lateinit var url: String

    @Value("\${spring.cloud.gcp.sql.jdbc-driver}")
    lateinit var driver: String

    override fun getJdbcUrl(): String {
        return url
    }

    override fun getJdbcDriverClass(): String {
        return driver
    }
}