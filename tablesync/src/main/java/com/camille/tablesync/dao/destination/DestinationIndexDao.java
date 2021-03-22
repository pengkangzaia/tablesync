package com.camille.tablesync.dao.destination;

import com.camille.tablesync.entity.IndexDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @FileName: DestinationIndexDao.java
 * @Description: DestinationIndexDao.java类说明
 * @Author: kang.peng
 * @Date: 2021/3/22 16:41
 */
@Mapper
public interface DestinationIndexDao {

    /**
     * 获取表中所有索引
     * @param tableName
     * @return
     */
    IndexDO getIndexByTableName(@Param("tableName") String tableName);

}
