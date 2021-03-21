package com.camille.tablesync.controller;

import com.camille.tablesync.entity.Field;
import com.camille.tablesync.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @FileName: FieldController.java
 * @Description: FieldController.java类说明
 * @Author: kang.peng
 * @Date: 2021/3/19 18:01
 */
@Controller
@RequestMapping("/field")
public class FieldController {

    @Autowired
    private FieldService fieldService;

    @ResponseBody
    @RequestMapping("/get/source")
    public List<Field> getSourceFieldInfo(String tableName) {
        List<Field> res = fieldService.getSourceFieldInfoByTableName(tableName);
        return res;
    }

    @ResponseBody
    @RequestMapping("/get/destination")
    public List<Field> getDestinationFieldInfo(String tableName) {
        List<Field> res = fieldService.getDestinationFieldInfoByTableName(tableName);
        return res;
    }

    @ResponseBody
    @RequestMapping("/get/diff")
    public String getDifferentFieldInfo(String tableName) {
        String sql = fieldService.getDiffFieldInfo(tableName);
        return sql;
    }

}
