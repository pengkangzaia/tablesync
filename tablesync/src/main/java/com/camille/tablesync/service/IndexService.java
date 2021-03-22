package com.camille.tablesync.service;

import com.camille.tablesync.dao.destination.DestinationIndexDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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



    /**
     * 索引有三种类型：主键索引，唯一索引，普通索引
     * 根据show index FROM `user`;查询索引名字
     * 根据建表语句获得表中索引，对比两个表的索引信息
     * 不存在的索引新建它
     * 存在的索引比较：索引名称，索引字段，索引类型，s索引方法，注释。
     * 如果有一个及其以上不同，修改这个索引
     * @param tableName 两个库中共同存在的表名
     * @return 同步索引的SQL
     */
    public String getIndexDiffSql(String tableName) {
        return null;
    }


}
