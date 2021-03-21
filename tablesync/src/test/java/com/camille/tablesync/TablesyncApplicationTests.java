package com.camille.tablesync;

import com.camille.tablesync.entity.Field;
import com.camille.tablesync.service.FieldService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class TablesyncApplicationTests {

    @Autowired
    private FieldService fieldService;

    @Test
    void contextLoads() {
    }

    @Test
    public void getDiffFieldTest() {
        String sql = fieldService.getDiffFieldInfo("user");
        System.out.println(sql);
    }

}
