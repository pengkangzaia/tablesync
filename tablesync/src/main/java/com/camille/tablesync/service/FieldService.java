package com.camille.tablesync.service;

import com.camille.tablesync.dao.FieldDao;
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
    private FieldDao fieldDao;

    public List<Field> getFieldInfoByTableName(String tableName) {
        if (tableName == null || "".equals(tableName)) {
            return new ArrayList<>();
        }
        List<Field> res = fieldDao.getFieldsByTableName(tableName);
        return res;
    }

}
