package com.camille.tablesync;

import com.camille.tablesync.service.SyncService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @FileName: SyncServiceTest.java
 * @Description: SyncServiceTest.java类说明
 * @Author: kang.peng
 * @Date: 2021/3/23 15:10
 */
@SpringBootTest
public class SyncServiceTest {

    @Autowired
    private SyncService syncService;

    @Test
    public void getSyncSqlTest() {
        String syncSql = syncService.getSyncSql();
        System.out.println(syncSql);
    }


}
