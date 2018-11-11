package com.triangl.dashboard.webservices.googleSQL

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gcp.autoconfigure.sql.CloudSqlJdbcInfoProvider
import org.springframework.context.annotation.Configuration


@Configuration
class CustomCloudSqlJdbcInfoProvider: CloudSqlJdbcInfoProvider {

    @Value("\${spring.cloud.gcp.sql.database-name}")
    val databaseName: String? = null

    @Value("\${spring.cloud.gcp.sql.instance-connection-name}")
    val instanceConnectionName: String? = null

    override fun getJdbcUrl(): String {
        return "jdbc:mysql://google/$databaseName?useLegacyDatetimeCode=false&cloudSqlInstance=$instanceConnectionName&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false"
    }

    override fun getJdbcDriverClass(): String {
        return "com.mysql.jdbc.Driver"
    }
}