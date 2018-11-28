package com.triangl.dashboard.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import java.util.*
import javax.sql.DataSource

@Profile("production")
@Configuration
@EnableJpaRepositories(basePackages = ["com.triangl.dashboard.repository.servingDB"], entityManagerFactoryRef = "servingDBEntityManager", transactionManagerRef = "servingDBTransactionManager")
class ServingDBConfig {
    @Autowired
    private val env: Environment? = null

    @Bean
    @Primary
    fun servingDBEntityManager(): LocalContainerEntityManagerFactoryBean {
        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = servingDBDataSource()
        em.setPackagesToScan("com.triangl.dashboard.dbModels.servingDB")

        val vendorAdapter = HibernateJpaVendorAdapter()
        em.jpaVendorAdapter = vendorAdapter
        val properties = HashMap<String, Any>()
        properties["hibernate.dialect"] = env!!.getProperty("hibernate.dialect")!!
        em.setJpaPropertyMap(properties)

        return em
    }

    @Primary
    @Bean
    fun servingDBDataSource(): DataSource {

        val dataSource = DriverManagerDataSource()

        dataSource.setDriverClassName(env!!.getProperty("spring.cloud.gcp.sql.jdbc-driver")!!)
        dataSource.url = env.getProperty("spring.cloud.gcp.sql.jdbc-url")!!
        dataSource.username = env.getProperty("spring.datasource.username")!!
        dataSource.password = env.getProperty("spring.datasource.password")!!

        return dataSource
    }

    @Primary
    @Bean
    fun servingDBTransactionManager(): PlatformTransactionManager {

        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = servingDBEntityManager().getObject()
        return transactionManager
    }
}