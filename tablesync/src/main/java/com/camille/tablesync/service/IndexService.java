package com.camille.tablesync.service;

import com.camille.tablesync.dao.destination.DestinationIndexDao;
import com.camille.tablesync.dao.source.SourceIndexDao;
import com.camille.tablesync.entity.IndexDO;
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
        List<IndexDO> sIndex = sourceIndexDao.getIndexByTableName(tableName);
        List<IndexDO> dIndex = destinationIndexDao.getIndexByTableName(tableName);
        Map<String, IndexDO> sIndexMap = sIndex.stream()
                .collect(Collectors.toMap(IndexDO::getKeyName, i -> i));
        Map<String, IndexDO> dIndexMap = dIndex.stream()
                .collect(Collectors.toMap(IndexDO::getKeyName, i -> i));
        // 查找源中不存在的索引
        StringBuilder sql = new StringBuilder();
        HashSet<IndexDO> notExist = new HashSet<>();
        if (CollectionUtils.isEmpty(sIndex)) {
            sql.append(addIndex(dIndex));
        } else {
            for (IndexDO d : dIndex) {
                if (!sIndexMap.containsKey(d.getKeyName())) {
                    // 生成新增索引的SQL
                    notExist.add(dIndexMap.get(d.getKeyName()));
                } else {
                    // 对比两个索引
                    IndexDO s = dIndexMap.get(d.getKeyName());
                    sql.append(modifyIndex(s, d));
                }
            }
        }
        return sql.toString();
    }

    private String modifyIndex(IndexDO s, IndexDO d) {
        return null;
    }

    private String addIndex(List<IndexDO> index) {
        return null;
    }


}
