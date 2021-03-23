package com.camille.tablesync.service;

import com.camille.tablesync.dao.destination.DestinationTableDao;
import com.camille.tablesync.dao.source.SourceTableDao;
import com.camille.tablesync.entity.CreateTableDO;
import com.camille.tablesync.utils.CommentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @FileName: TableService.java
 * @Description: TableService.java类说明
 * @Author: camille
 * @Date: 2021/3/21 20:05
 */
@Service
public class TableService {

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
                    // 插入注释
                    sqls.add(CommentUtils.getComment(destinationName, "CREATE"));
                    sqls.add(createTableSql.getCreateTable());
                    // 插入换行
                    sqls.add(CommentUtils.getLineFeeds(3));
                }
            }
        }
        return sqls;
    }


    /**
     * 获取两个库中表名相同的表
     * @param sourceDbName
     * @param destinationDbName
     * @return
     */
    public List<String> getDiffTableName(String sourceDbName, String destinationDbName) {
        List<String> sourceTableNames = getTableNames(sourceDbName);
        List<String> destinationTableNames = getTableNames(destinationDbName);
        if (CollectionUtils.isEmpty(sourceTableNames) || CollectionUtils.isEmpty(destinationTableNames)) {
            return new ArrayList<>();
        }
        List<String> sameTableNames = sourceTableNames.stream()
                .filter(destinationTableNames::contains).collect(Collectors.toList());
        return sameTableNames;
    }



}
