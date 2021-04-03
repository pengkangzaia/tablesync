package com.camille.tablesync.utils;

import com.alibaba.fastjson.JSON;
import com.camille.tablesync.entity.Field;
import org.apache.catalina.LifecycleState;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @FileName: FileGenUtils.java
 * @Description: FileGenUtils.java类说明
 * @Author: camille
 * @Date: 2021/4/3 15:59
 */
public class FileGenUtils {


    public static final String TXT_SUFFIX = ".txt";

    public static final String SQL_SUFFIX = ".sql";

    public static final String SPACE = " ";

    public static final String EQUATION = "=";

    public static final String COMMA = ",";

    /**
     * 分号
     */
    public static final String SEMICOLON = ";";

    /**
     * 把源集合转化为txt文件
     * @param source
     */
    public static void fileGenByList(List<String> source) {
        try {
            if (!CollectionUtils.isEmpty(source)) {
                // UUID产生随机名称
                String fileName = UUID.randomUUID().toString().replace("-", "") + TXT_SUFFIX;
                // 目录必须存在，文件可以不存在。文件不存在会自动创建
                File file = new File(".\\export\\" + fileName);
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                for (String record : source) {
                    bw.write(record + "\r\n");
                }
                bw.flush();
                bw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // update table_name set column1 = x, column2 = y;
    // UPDATE table_name
    // SET column1=value, column2=value2,...
    // WHERE some_column=some_value

    /**
     * 生成更新SQL的脚本
     * @param tableName 待更新的表名
     * @param records 待更新的记录
     * @param condition 更新条件
     */
    public static void updateSqlGenByObjects(String tableName, List<Field> records, String condition) {
        try {
            if (!CollectionUtils.isEmpty(records)) {

                String fileName = UUID.randomUUID().toString().replace("-", "") + SQL_SUFFIX;
                File file = new File(".\\export\\" + fileName);
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));

                for (Field record : records) {
                    /*StringBuilder sb = new StringBuilder();
                    sb.append("update" + SPACE).append(tableName).append(SPACE).append("set").append(SPACE);
                    sb.append("Field").append(EQUATION).append(record.getFieldName()).append(COMMA).append(SPACE);
                    sb.append("Type").append(EQUATION).append(record.getFieldType()).append(COMMA).append(SPACE);
                    sb.append("Null").append(EQUATION).append(record.getFieldAllowNull()).append(COMMA).append(SPACE);
                    sb.append("Key").append(EQUATION).append(record.getFieldKey()).append(COMMA).append(SPACE);
                    sb.append("Default").append(EQUATION).append(record.getFieldDefault()).append(COMMA).append(SPACE);
                    sb.append("Extra").append(EQUATION).append(record.getFieldExtra()).append(SPACE);
                    sb.append("where").append(SPACE).append(condition).append(SEMICOLON).append("\r\n");*/
                    String sb = "update" + SPACE + tableName + SPACE + "set" + SPACE +
                            "Field" + EQUATION + record.getFieldName() + COMMA + SPACE +
                            "Type" + EQUATION + record.getFieldType() + COMMA + SPACE +
                            "Null" + EQUATION + record.getFieldAllowNull() + COMMA + SPACE +
                            "Key" + EQUATION + record.getFieldKey() + COMMA + SPACE +
                            "Default" + EQUATION + record.getFieldDefault() + COMMA + SPACE +
                            "Extra" + EQUATION + record.getFieldExtra() + SPACE +
                            "where" + SPACE + condition + SEMICOLON + "\r\n";
                    bw.write(sb);
                }
                bw.flush();
                bw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    public static void main(String[] args) {
        List<Field> source = new ArrayList<>();
        Field field1 = new Field("a", "b", "c", "d","e", "f");
        Field field2 = new Field("a", "b", "c", "d","e", "f");
        Field field3 = new Field("a", "b", "c", "d","e", "f");
        Field field4 = new Field("a", "b", "c", "d","e", "f");
        Field field5 = new Field("a", "b", "c", "d","e", "f");
        source.add(field1);
        source.add(field2);
        source.add(field3);
        source.add(field4);
        source.add(field5);
        try {
            updateSqlGenByObjects("table1", source, "a=b");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("生成文件异常");
        }
    }



}
