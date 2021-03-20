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
 * @FileName: SourceDataSourceConfig.java
 * @Description: SourceDataSourceConfig.java类说明
 * @Author: camille
 * @Date: 2021/3/20 22:29
 */
@Configuration
@MapperScan(basePackages = {"com.camille.tablesync.dao.source"}, sqlSessionFactoryRef = "sourceSqlSessionFactory")
public class SourceDataSourceConfig {

    @Autowired
    @Qualifier("sourceDb")
    private DataSource sourceDataSource;

    @Bean
    public SqlSessionFactory sourceSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(sourceDataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/source/*.xml"));
        return factoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplateSource() throws Exception {
        return new SqlSessionTemplate(sourceSqlSessionFactory());
    }


}
