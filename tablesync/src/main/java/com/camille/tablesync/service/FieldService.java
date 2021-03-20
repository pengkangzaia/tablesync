package com.camille.tablesync.service;

import com.camille.tablesync.dao.destination.DestinationFieldDao;
import com.camille.tablesync.dao.source.SourceFieldDao;
import com.camille.tablesync.entity.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<Field> getSourceFieldInfoByTableName(String tableName) {
        if (tableName == null || "".equals(tableName)) {
            return new ArrayList<>();
        }
        List<Field> res = sourceFieldDao.getFieldsByTableName(tableName);
        return res;
    }

    public List<Field> getDestinationFieldInfoByTableName(String tableName) {
        if (tableName == null || "".equals(tableName)) {
            return new ArrayList<>();
        }
        List<Field> res = destinationFieldDao.getFieldsByTableName(tableName);
        return res;
    }

}
