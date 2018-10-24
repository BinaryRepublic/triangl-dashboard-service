package com.triangl.dashboard.webservices.googleSQL

import org.springframework.cloud.gcp.autoconfigure.sql.CloudSqlJdbcInfoProvider
import org.springframework.context.annotation.Configuration


@Configuration
class CustomCloudSqlJdbcInfoProvider: CloudSqlJdbcInfoProvider {
    override fun getJdbcUrl(): String {
        return "jdbc:mysql://google/serving-prod?useLegacyDatetimeCode=false&cloudSqlInstance=triangl-215714:europe-west3:analyzing&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false"
    }

    override fun getJdbcDriverClass(): String {
        return "com.mysql.jdbc.Driver"
    }
}