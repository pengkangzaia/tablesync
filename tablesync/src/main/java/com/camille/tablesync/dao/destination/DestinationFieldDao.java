package com.camille.tablesync.dao.destination;

import com.camille.tablesync.entity.Field;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @FileName: FieldDao.java
 * @Description: FieldDao.java类说明
 * @Author: kang.peng
 * @Date: 2021/3/19 17:49
 */
@Mapper
public interface DestinationFieldDao {

    /**
     * 根据表名获取表中字段信息
     * @param tableName 表名
     * @return
     */
    List<Field> getFieldsByTableName(@Param("tableName") String tableName);

}
