package com.camille.tablesync;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TablesyncApplication {

    public static void main(String[] args) {

        SpringApplication.run(TablesyncApplication.class, args);

    }




}
