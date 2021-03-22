package com.camille.tablesync.service;

import com.camille.tablesync.dao.destination.DestinationFieldDao;
import com.camille.tablesync.dao.source.SourceFieldDao;
import com.camille.tablesync.entity.Field;
import com.camille.tablesync.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

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
        Map<String, Field> sourceMap = new HashMap<>(source.size());
        for (Field field : source) {
            sourceMap.put(field.getFieldName(), field);
        }
        // 源数据库中为线上部署的旧版本，目标数据库为；本地测试的新版本。需要更新在源中不存在的字段
        Set<String> notExist = new HashSet<>();
        Set<String> diff = new HashSet<>();
        for (Field d : destination) {
            if (!sourceMap.containsKey(d.getFieldName())) {
                // 1.添加不存在的字段
                notExist.add(d.getFieldName());
            } else {
                // 2.修改不一致的字段
                diff.add(d.getFieldName());
            }
        }
        // 生成添加和修改字段的SQL
        return handleField(tableName, notExist, diff);
    }



    private String handleField(String tableName, Set<String> notExist, Set<String> diff) {
        if (CollectionUtils.isEmpty(notExist) && CollectionUtils.isEmpty(diff)) {
            return null;
        }
        StringBuilder sql = new StringBuilder("ALTER TABLE " + tableName + " \n");
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
                sql.append(" CHANGE ").append(fieldName).append(" ").append(lines[i]).append("\n");
            }
        }
        // 需要遵守先修改再添加的原则，防止主键冲突
        for (int i = 1; i < lines.length; i++) {
            String trimLine = lines[i].trim();
            String fieldName = trimLine.substring(0, trimLine.indexOf(" "));
            fieldName = fieldName.substring(1, fieldName.length() - 1);
            if (notExist.contains(fieldName)) {
                // 需要添加到增加字段的SQL
                sql.append(" ADD ").append(lines[i]).append("\n");
            }
        }
        // 如果最后一个符号为逗号，处理最后一个逗号，改为句号
        if (sql.charAt(sql.length() - 1) == ',') {
            sql.replace(sql.lastIndexOf(","), sql.lastIndexOf(","), ";");
        } else {
            sql.deleteCharAt(sql.length() - 1);
            sql.append(';');
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
        return "ALTER TABLE " + tableName + " DROP PRIMARY KEY;" + "\n" +
                "ALTER TABLE " + tableName + " ADD PRIMARY KEY(" + field.getFieldName() + ");" + "\n";
    }


        /**
         * CREATE TABLE `user` (
         *   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
         *   `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
         *   `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
         *   `nick_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户昵称-用户显示',
         *   `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '用户手机号码',
         *   `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
         *   `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态：可用:1，不可用:0，冻结:-1，过期:-2',
         *   `modify_user_id` bigint(20) DEFAULT NULL,
         *   `create_user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '账号创建人员',
         *   `gmt_freeze_end` datetime DEFAULT NULL COMMENT '冻结结束时间',
         *   `effective_start_time` datetime DEFAULT NULL COMMENT '账号有效的开始时间(可以为null)',
         *   `effective_end_time` datetime DEFAULT NULL COMMENT '账号有效的结束时间(可以为null)',
         *   `last_login_time` datetime DEFAULT NULL COMMENT '账号最后登陆时间(可以为null)',
         *   `modify_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
         *   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
         *   `extro_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '其他信息',
         *   `auto_distribution` int(1) NOT NULL DEFAULT '0',
         *   `last_modify_password_time` datetime DEFAULT NULL COMMENT '最后一次修改密码时间',
         *   `login_times` tinyint(1) DEFAULT '0' COMMENT '当天登录次数',
         *   `password_error_times` tinyint(1) DEFAULT '0' COMMENT '当天密码错误次数',
         *   `current_dept_id` bigint(20) DEFAULT NULL COMMENT '用户当前所在部门',
         *   PRIMARY KEY (`id`) USING BTREE,
         *   UNIQUE KEY `un_user_name` (`user_name`) USING BTREE COMMENT '用户名唯一'
         * ) ENGINE=InnoDB AUTO_INCREMENT=345 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户表'
         */



}
