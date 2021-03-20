package com.camille.tablesync.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @FileName: DestinationDataSourceConfig.java
 * @Description: DestinationDataSourceConfig.java类说明
 * @Author: camille
 * @Date: 2021/3/20 22:36
 */
@Configuration
@MapperScan(basePackages = "com.camille.tablesync.dao.destination", sqlSessionFactoryRef = "destinationSqlSessionFactory")
public class DestinationDataSourceConfig {

    @Autowired
    @Qualifier("destinationDb")
    private DataSource destinationDataSource;

    @Bean
    public SqlSessionFactory destinationSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(destinationDataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/destination/*.xml"));
        return factoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplateDestination() throws Exception {
        return new SqlSessionTemplate(destinationSqlSessionFactory());
    }


}
