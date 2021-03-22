package com.camille.tablesync.dao.source;

import com.camille.tablesync.entity.IndexDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @FileName: SourceIndexDao.java
 * @Description: SourceIndexDao.java类说明
 * @Author: kang.peng
 * @Date: 2021/3/22 16:41
 */
@Mapper
public interface SourceIndexDao {

    /**
     * 获取表中所有索引
     * @param tableName
     * @return
     */
    List<IndexDO> getIndexByTableName(@Param("tableName") String tableName);

}
