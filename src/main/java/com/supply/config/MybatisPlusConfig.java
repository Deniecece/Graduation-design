package com.supply.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * <p>
 *  SpringBoot集成MyBatis-Plus工具类
 * </p>
 *
 * @author Deniecece
 * @since 2019-10-09
 */
@EnableTransactionManagement
@Configuration
@MapperScan("com.supply.mapper*")
public class MybatisPlusConfig {

    /**
     * Spring事务的注解配置
     * 　　把DataSource（如DruidDataSource）作为一个@Bean注册到Spring容器中，配置好事务性资源。
     * 　　把@EnableTransactionManagement注解放到一个@Configuration类上，配置好事务管理器，并启用事务管理。
     * 　　把@Transactional注解放到类上或方法上，可以设置注解的属性，表明该方法按配置好的属性参与到事务中。
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return new HikariDataSource();
    }


    /**
     * mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDialectType("mysql");
        return page;
    }

    /**
     * mybatis-plus乐观锁插件
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        OptimisticLockerInterceptor oLocker = new OptimisticLockerInterceptor();
        return oLocker;
    }

    @Value("${mybatis.max-time}")
    private Long maxTime;

    /**
     * SQL执行效率插件
     */
    @Bean
//    @Profile({"dev", "test"})// 设置 dev test 环境开启
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        performanceInterceptor.setMaxTime(maxTime);
        performanceInterceptor.setFormat(true);
        return performanceInterceptor;
    }

    /**
     * 设置驼峰命名规则
     * @return
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            //设置驼峰命名规则
            configuration.setMapUnderscoreToCamelCase(true);
        };
    }
}
