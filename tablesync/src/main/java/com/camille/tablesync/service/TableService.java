package com.camille.tablesync.service;

import com.camille.tablesync.dao.destination.DestinationTableDao;
import com.camille.tablesync.dao.source.SourceTableDao;
import com.camille.tablesync.entity.CreateTableDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @FileName: TableService.java
 * @Description: TableService.java类说明
 * @Author: camille
 * @Date: 2021/3/21 20:05
 */
@Service
public class TableService {

    @Value("${spring.datasource.source.name}")
    private String sourceDbName;

    @Value("${spring.datasource.destination.name}")
    private String destinationDbName;

    @Autowired
    private SourceTableDao sourceTableDao;

    @Autowired
    private DestinationTableDao destinationDao;

    public List<String> getTableNames(String dbName) {
        List<String> tableNames = sourceTableDao.getTableNames(dbName);
        return tableNames;
    }

    public String getCreateTableSql(String tableName, boolean isSource) {
        if (isSource) {
            CreateTableDO tableDO = sourceTableDao.getCreateTableSql(tableName);
            return tableDO.getCreateTable();
        } else {
            CreateTableDO tableDO = destinationDao.getCreateTableSql(tableName);
            return tableDO.getCreateTable();
        }
    }



    /**
     * 返回源数据库中不存在的表的创建DDL
     * @param sourceDbName
     * @param destinationDbName
     * @return
     */
    public List<String> getDiffTableSql(String sourceDbName, String destinationDbName) {
        List<String> sourceNames = getTableNames(sourceDbName);
        List<String> destinationNames = getTableNames(destinationDbName);
        // 源数据库中为线上部署的旧版本，目标数据库为；本地测试的新版本。需要更新在源中不存在的表
        HashSet<String> sourceSet = new HashSet<>(sourceNames);
        List<String> sqls = new ArrayList<>();
        if (!CollectionUtils.isEmpty(destinationNames)) {
            for (String destinationName : destinationNames) {
                if (!sourceSet.contains(destinationName)) {
                    CreateTableDO createTableSql = destinationDao.getCreateTableSql(destinationName);
                    sqls.add(createTableSql.getCreateTable());
                }
            }
        }
        return sqls;
    }



}
