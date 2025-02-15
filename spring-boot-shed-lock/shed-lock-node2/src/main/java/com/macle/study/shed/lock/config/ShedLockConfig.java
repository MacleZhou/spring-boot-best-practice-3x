package com.macle.study.shed.lock.config;

import jakarta.annotation.Resource;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class ShedLockConfig {

    @Resource
    private Environment env;

    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(
                JdbcTemplateLockProvider.Configuration.builder()
                        .withJdbcTemplate(new JdbcTemplate(dataSource))
                        .withLockedByValue(System.getProperty("server.name") + ":" + env.getProperty("server.port"))
                        .usingDbTime() // Works with PostgreSQL, MySQL, MariaDb, MS SQL, Oracle, HSQL, H2, DB2, and others
                        .build()
        );

        /**
         * If self define the lock table
         * */
        /*
        new JdbcTemplateLockProvider(builder()
                .withTableName("shdlck")
                .withColumnNames(new ColumnNames("n", "lck_untl", "lckd_at", "lckd_by"))
                .withJdbcTemplate(new JdbcTemplate(getDatasource()))
                .withLockedByValue("my-value")
                .withDbUpperCase(true)
                .build())*/
    }

    /**Redis*/
    /*
    @Bean
    public LockProvider lockProvider(ReactiveRedisConnectionFactory connectionFactory) {
        return new ReactiveRedisLockProvider.Builder(connectionFactory)
                .environment(ENV)
                .build();
    }
    */
}
