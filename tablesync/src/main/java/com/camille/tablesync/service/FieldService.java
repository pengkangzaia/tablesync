package com.camille.tablesync.service;

import com.camille.tablesync.constant.Constants;
import com.camille.tablesync.dao.destination.DestinationFieldDao;
import com.camille.tablesync.dao.source.SourceFieldDao;
import com.camille.tablesync.entity.Field;
import com.camille.tablesync.utils.CommentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    @Value("${drop.enable:false}")
    private boolean dropEnable;

    @Autowired
    private SourceFieldDao sourceFieldDao;

    @Autowired
    private DestinationFieldDao destinationFieldDao;

    @Autowired
    private TableService tableService;

    public List<Field> getSourceFieldInfoByTableName(String tableName) {
        if (tableName == null || "".equals(tableName)) {
            return new ArrayList<>();
        }
        return sourceFieldDao.getFieldsByTableName(tableName);
    }

    public List<Field> getDestinationFieldInfoByTableName(String tableName) {
        if (tableName == null || "".equals(tableName)) {
            return new ArrayList<>();
        }
        return destinationFieldDao.getFieldsByTableName(tableName);
    }

    public String getDiffFieldInfo(String tableName) {
        if (tableName == null || "".equals(tableName)) {
            return null;
        }
        List<Field> source = sourceFieldDao.getFieldsByTableName(tableName);
        List<Field> destination = destinationFieldDao.getFieldsByTableName(tableName);
        Map<String, Field> sourceMap = source.stream().collect(Collectors.toMap(Field::getFieldName, f -> f));
        Map<String, Field> destinationMap = destination.stream().collect(Collectors.toMap(Field::getFieldName, f -> f));

        // 源数据库中为线上部署的旧版本，目标数据库为；本地测试的新版本。需要更新在源中不存在的字段
        Set<String> notExist = new HashSet<>();
        Set<String> diff = new HashSet<>();
        Set<String> needDelete = new HashSet<>();

        // 如果设置了删除，生成字段的SQL
        if (dropEnable) {
            for (Field s : source) {
                String sFieldName = s.getFieldName();
                if (!destinationMap.containsKey(sFieldName)) {
                    needDelete.add(sFieldName);
                }
            }
        }


        for (Field d : destination) {
            String dFieldName = d.getFieldName();
            if (!sourceMap.containsKey(dFieldName)) {
                // 1.添加不存在的字段
                notExist.add(dFieldName);
            } else {
                // 2.修改不一致的字段
                diff.add(dFieldName);
            }
        }

        // 修改字段的SQL
        return handleField(tableName, notExist, diff, needDelete);
    }





    private String handleField(String tableName, Set<String> notExist, Set<String> diff, Set<String> needDelete) {
        if (CollectionUtils.isEmpty(notExist) && CollectionUtils.isEmpty(diff) && CollectionUtils.isEmpty(needDelete)) {
            return null;
        }

        StringBuilder dropSql = new StringBuilder();
        if (dropEnable) {
            dropSql.append(CommentUtils.getComment(tableName, "DROP"));
            dropSql.append("ALTER TABLE ").append(tableName).append(" \n");
            for (String fieldName : needDelete) {
                dropSql.append(" DROP ").append(fieldName).append(",\n");
            }
            // 处理末尾字符, 去除最后一个逗号和换行符
            dropSql.delete(dropSql.lastIndexOf(","), dropSql.length()).append(";");
            dropSql.append(CommentUtils.getLineFeeds(3));
        }

        StringBuilder modifySql = new StringBuilder();
        modifySql.append(CommentUtils.getComment(tableName, "ALTER"));
        modifySql.append("ALTER TABLE ").append(tableName).append(" \n");
        String createTableSql = tableService.getCreateTableSql(tableName, false);
        int firstLeftBracket = createTableSql.indexOf("(");
        int fistRightBracket = createTableSql.lastIndexOf(")");
        createTableSql = createTableSql.substring(firstLeftBracket + 1, fistRightBracket);
        String[] lines = createTableSql.split("\n");
        // 第一行为空，不考虑
        for (int i = 1; i < lines.length; i++) {
            String trimLine = lines[i].trim();
            String fieldName = trimLine.substring(0, trimLine.indexOf(" "));
            // 去除两个单引号
            fieldName = fieldName.substring(1, fieldName.length() - 1);
            if (diff.contains(fieldName)) {
                // 需要添加到修改字段的SQL
                modifySql.append(" CHANGE ").append(fieldName).append(" ").append(lines[i]).append("\n");
            }
        }
        // 需要遵守先修改再添加的原则，防止主键冲突
        for (int i = 1; i < lines.length; i++) {
            String trimLine = lines[i].trim();
            String fieldName = trimLine.substring(0, trimLine.indexOf(" "));
            fieldName = fieldName.substring(1, fieldName.length() - 1);
            if (notExist.contains(fieldName)) {
                // 需要添加到增加字段的SQL
                modifySql.append(" ADD ").append(lines[i]).append("\n");
            }
        }
        // 去除最后一个换行符
        modifySql.deleteCharAt(modifySql.length() - 1);
        // 如果最后一个符号为逗号，处理最后一个逗号，改为句号
        if (modifySql.charAt(modifySql.length() - 1) == Constants.COMMA) {
            modifySql.deleteCharAt(modifySql.length() - 1);
        }
        modifySql.append(';');
        modifySql.append(CommentUtils.getLineFeeds(3));

        return dropSql.append(modifySql).toString();
    }


}
