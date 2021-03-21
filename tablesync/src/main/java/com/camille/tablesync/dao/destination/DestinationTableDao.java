package com.camille.tablesync.dao.destination;

import com.camille.tablesync.entity.CreateTableDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @FileName: DestinationTableDao.java
 * @Description: DestinationTableDao.java类说明
 * @Author: camille
 * @Date: 2021/3/21 19:55
 */
@Mapper
public interface DestinationTableDao {

    /**
     * 获取数据库中所有表名
     * @param dbName
     * @return
     */
    List<String> getTableNames(@Param("dbName") String dbName);

    /**
     * 获取创建表的SQL
     * @param tableName
     * @return
     */
    CreateTableDO getCreateTableSql(@Param("tableName") String tableName);


}
