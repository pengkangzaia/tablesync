package com.camille.tablesync.service;

import com.camille.tablesync.utils.CommentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @FileName: SyncService.java
 * @Description: SyncService.java类说明
 * @Author: kang.peng
 * @Date: 2021/3/23 14:25
 */
@Service
public class SyncService {

    @Value("${spring.datasource.source.name}")
    private String sourceDbName;

    @Value("${spring.datasource.destination.name}")
    private String destinationDbName;

    @Autowired
    private TableService tableService;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private IndexService indexService;

    public String getSyncSql() {
        StringBuilder res = new StringBuilder();
        // 新建表的SQL语句
        List<String> diffTableSql = tableService.getDiffTableSql(sourceDbName, destinationDbName);
        if (diffTableSql != null) {
            for (String sql : diffTableSql) {
                res.append(sql);
            }
        }

        List<String> diffTableName = tableService.getDiffTableName(sourceDbName, destinationDbName);
        if (!CollectionUtils.isEmpty(diffTableName)) {
            for (String name : diffTableName) {
                String sql;
                if((sql = fieldService.getDiffFieldInfo(name)) != null) {
                    // 修改字段的SQL语句
                    res.append(CommentUtils.getComment(name, "ALTER"));
                    res.append(sql);
                    res.append(CommentUtils.getLineFeeds(3));
                }
                if ((sql = indexService.getIndexDiffSql(name)) != null) {
                    // 修改索引的SQL语句
                    res.append(CommentUtils.getComment(name, "ALTER"));
                    res.append(sql);
                    res.append(CommentUtils.getLineFeeds(3));
                }
            }
        }

        return res.toString();
    }



}
