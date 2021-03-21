package com.camille.tablesync.service;

import com.camille.tablesync.dao.destination.DestinationFieldDao;
import com.camille.tablesync.dao.source.SourceFieldDao;
import com.camille.tablesync.entity.Field;
import com.camille.tablesync.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.FeatureDescriptor;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @FileName: FieldService.java
 * @Description: FieldService.java类说明
 * @Author: kang.peng
 * @Date: 2021/3/19 17:58
 */
@Service
public class FieldService {

    @Autowired
    private SourceFieldDao sourceFieldDao;

    @Autowired
    private DestinationFieldDao destinationFieldDao;

    public List<Field> getSourceFieldInfoByTableName(String tableName) {
        if (tableName == null || "".equals(tableName)) {
            return new ArrayList<>();
        }
        List<Field> res = sourceFieldDao.getFieldsByTableName(tableName);
        return res;
    }

    public List<Field> getDestinationFieldInfoByTableName(String tableName) {
        if (tableName == null || "".equals(tableName)) {
            return new ArrayList<>();
        }
        List<Field> res = destinationFieldDao.getFieldsByTableName(tableName);
        return res;
    }

    public String getDiffFieldInfo(String tableName) {
        if (tableName == null || "".equals(tableName)) {
            return null;
        }
        List<Field> source = sourceFieldDao.getFieldsByTableName(tableName);
        List<Field> destination = destinationFieldDao.getFieldsByTableName(tableName);
        Map<String, Field> sourceMap = new HashMap<>(source.size());
        for (Field field : source) {
            sourceMap.put(field.getFieldName(), field);
        }
        // 源数据库中为线上部署的旧版本，目标数据库为；本地测试的新版本。需要更新在源中不存在的字段
        List<Field> notExist = new ArrayList<>();
        List<Pair<Field>> diff = new ArrayList<>();
        for (Field d : destination) {
            if (!sourceMap.containsKey(d.getFieldName())) {
                // 1.添加不存在的字段
                notExist.add(d);
            } else {
                // 2.修改不一致的字段
                Field s = sourceMap.get(d.getFieldName());
                diff.add(new Pair<Field>(s, d));
            }
        }
        for (Field field : notExist) {
            System.out.println(field);
        }
        System.out.println();
        for (Pair<Field> fieldPair : diff) {
            System.out.println(fieldPair.getSourceData());
            System.out.println(fieldPair.getDestinationData());
        }

        StringBuilder sql = new StringBuilder();
        for (Field field : notExist) {
            sql.append(addField(tableName, field));
            if (field.getFieldKey().equals("PRI")) {
                sql.append(modifyPrimaryKey(tableName, field));
            }
        }

        return sql.toString();
    }

    /**
     * 处理主键
     * @param tableName 表名
     * @param field 含有新主键的字段
     * @return 执行的SQL脚本
     */
    private String modifyPrimaryKey(String tableName, Field field) {
        // 对主键需要删除原来的主键之后，再新建一个
        // alter table user drop primary key;
        // alter table user add primary key(username)
        StringBuilder sql = new StringBuilder();
        sql.append("ALTER TABLE ").append(tableName).append(" DROP PRIMARY KEY;").append("\n");
        sql.append("ALTER TABLE ").append(tableName)
                .append(" ADD PRIMARY KEY(").append(field.getFieldName()).append(");").append("\n");
        return sql.toString();
    }

    private String addField(String tableName, Field field) {
        StringBuilder sql = new StringBuilder();
        sql.append("ALTER TABLE ").append(tableName)
                .append(" ADD COLUMN ").append(field.getFieldName()).append(" ")
                .append(field.getFieldType());
        // 考虑是否非空
        if (field.getFieldAllowNull().equals("NO")) {
            sql.append(" NOT NULL ");
        }
        // todo 还需考虑字段的其他属性，比如说字段是否是自增的，字段是否有默认值，字段的索引等等
        sql.append(";\n");
        return sql.toString();
    }


}
