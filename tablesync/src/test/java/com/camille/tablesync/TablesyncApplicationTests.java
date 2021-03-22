package com.camille.tablesync;

import com.camille.tablesync.service.FieldService;
import com.camille.tablesync.service.TableService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class TablesyncApplicationTests {

    @Autowired
    private FieldService fieldService;

    @Autowired
    private TableService tableService;

    @Test
    void contextLoads() {
    }

    @Test
    public void getDiffFieldTest() {
        String sql = fieldService.getDiffFieldInfo("user");
        System.out.println(sql);
    }

    @Test
    public void getTableNameTest() {
        List<String> tableNames = tableService.getTableNames("source");
        for (String tableName : tableNames) {
            System.out.println(tableName);
        }
        for (String tableName : tableNames) {
            String createTableSql = tableService.getCreateTableSql(tableName, true);
            System.out.println(createTableSql);
        }
    }

    @Test
    public void getDiffTableSql() {
        List<String> diffTableSql = tableService.getDiffTableSql("source", "destination");
        if (diffTableSql != null) {
            for (String s : diffTableSql) {
                System.out.println(s);
            }
        }
    }



}
