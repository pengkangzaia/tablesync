package com.camille.tablesync.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @FileName: DataSourceConfig.java
 * @Description: 创建自定义数据源
 * @Author: camille
 * @Date: 2021/3/20 21:54
 */
@Configuration
public class DataSourceConfig {
    

    @Bean(name = "sourceDb")
    @ConfigurationProperties(prefix = "spring.datasource.source")
    public DataSource sourceDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "destinationDb")
    @ConfigurationProperties(prefix = "spring.datasource.destination")
    public DataSource destinationDataSource() {
        return DataSourceBuilder.create().build();
    }

}
