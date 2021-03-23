package com.camille.tablesync.service;

import com.camille.tablesync.dao.destination.DestinationIndexDao;
import com.camille.tablesync.dao.source.SourceIndexDao;
import com.camille.tablesync.entity.IndexDO;
import com.camille.tablesync.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @FileName: IndexService.java
 * @Description: IndexService.java类说明
 * @Author: kang.peng
 * @Date: 2021/3/22 16:32
 */
@Service
public class IndexService {

    @Autowired
    private DestinationIndexDao destinationIndexDao;

    @Autowired
    private SourceIndexDao sourceIndexDao;

    /**
     * 索引有三种类型：主键索引，唯一索引，普通索引
     * 根据show index FROM `user`;查询索引名字
     * 根据建表语句获得表中索引，对比两个表的索引信息
     * 不存在的索引新建它
     * 存在的索引比较：索引名称，索引字段，索引类型，索引方法，注释。
     * 如果有一个及其以上不同，修改这个索引
     * @param tableName 两个库中共同存在的表名
     * @return 同步索引的SQL
     */
    public String getIndexDiffSql(String tableName) {
        // todo 处理组合索引的特殊情况
        List<IndexDO> sIndex = sourceIndexDao.getIndexByTableName(tableName);
        List<IndexDO> dIndex = destinationIndexDao.getIndexByTableName(tableName);
        Map<String, IndexDO> sIndexMap = sIndex.stream()
                .collect(Collectors.toMap(IndexDO::getKeyName, i -> i));
        Map<String, IndexDO> dIndexMap = dIndex.stream()
                .collect(Collectors.toMap(IndexDO::getKeyName, i -> i));
        StringBuilder sql = new StringBuilder();


        if (CollectionUtils.isEmpty(sIndex) && CollectionUtils.isEmpty(dIndex)) {
            return sql.toString();
        }
        if (CollectionUtils.isEmpty(sIndex)) {
            for (IndexDO index : dIndex) {
                sql.append(addIndex(index));
            }
            return sql.toString();
        }

        // 添加不存在的索引
        HashSet<IndexDO> notExist = new HashSet<>();
        // 删除同名索引之后再添加新索引
        HashSet<Pair<IndexDO>> diff = new HashSet<>();
        for (IndexDO d : dIndex) {
            if (!sIndexMap.containsKey(d.getKeyName())) {
                // 生成新增索引的SQL
                notExist.add(dIndexMap.get(d.getKeyName()));
            } else {
                // 修改原来的索引，处理对象是源数据库的索引
                diff.add(new Pair<>(sIndexMap.get(d.getKeyName()), d));
            }
        }

        // 先修改再添加，防止存在多个主键索引
        for (Pair<IndexDO> pair : diff) {
            sql.append(modifyIndex(pair.getSourceData(), pair.getDestinationData()));
        }
        for (IndexDO indexDO : notExist) {
            sql.append(addIndex(indexDO));
        }

        return sql.toString();
    }

    private String modifyIndex(IndexDO sIndex, IndexDO dIndex) {
        String sql = "";
        sql = dropIndex(sIndex);
        sql += addIndex(dIndex);
        return sql;
    }


    private String addIndex(IndexDO indexDO) {
        String sql = "";
        if (indexDO == null) { return null; }
        if (indexDO.isPrimaryKey()) {
            sql = "ALTER TABLE " + indexDO.getTable() + " ADD PRIMARY KEY (" + indexDO.getColumnName() + ");\n";
        } else if (indexDO.isUniqueKey()) {
            sql = "ALTER TABLE " + indexDO.getTable() + " ADD UNIQUE KEY (" + indexDO.getColumnName() + ");\n";
        } else if (indexDO.isNormalKey()) {
            sql = "ALTER TABLE " + indexDO.getTable() + " ADD KEY " + indexDO.getKeyName() + "("  + indexDO.getColumnName() + ");\n";
        }
        // todo 考虑外键
        return sql;
    }

    private String dropIndex(IndexDO indexDO) {
        String sql = "";
        if (indexDO == null) { return null; }
        if (indexDO.isPrimaryKey()) {
            sql = "ALTER TABLE " + indexDO.getTable() + " DROP PRIMARY KEY;\n";
        } else {
            sql = "ALTER TABLE " + indexDO.getTable() + " DROP INDEX " + indexDO.getColumnName() + ";\n";
        }
        return sql;
    }



}
