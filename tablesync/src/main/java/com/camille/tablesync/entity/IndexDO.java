package com.camille.tablesync.entity;

import lombok.Data;

/**
 * @FileName: IndexDO.java
 * @Description: IndexDO.java类说明
 * @Author: kang.peng
 * @Date: 2021/3/22 16:44
 */
@Data
public class IndexDO {

    private String table;
    private Short nonUnique;
    private String keyName;
    private String columnName;
    private String indexType;


}