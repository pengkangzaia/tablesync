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
    /**
     * 是否为唯一索引，0是，1不是
     */
    private Short nonUnique;
    private String keyName;
    /**
     * 在组合索引中的位置，如果不是组合索引的一部分则是1
     */
    private Integer seqInIndex;
    private String columnName;
    private String indexType;

    public boolean isPrimaryKey() {
        return nonUnique == 0 && "PRIMARY".equals(keyName);
    }

    public boolean isUniqueKey() {
        return nonUnique == 0 && !"PRIMARY".equals(keyName);
    }

    public boolean isNormalKey() {
        return nonUnique == 1;
    }


}
