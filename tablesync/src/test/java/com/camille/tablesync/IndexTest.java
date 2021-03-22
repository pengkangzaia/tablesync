package com.camille.tablesync;

import com.camille.tablesync.dao.destination.DestinationIndexDao;
import com.camille.tablesync.dao.source.SourceIndexDao;
import com.camille.tablesync.entity.IndexDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @FileName: IndexTest.java
 * @Description: IndexTest.java类说明
 * @Author: kang.peng
 * @Date: 2021/3/22 17:06
 */
@SpringBootTest
public class IndexTest {

    @Autowired
    private DestinationIndexDao destinationIndexDao;

    @Autowired
    private SourceIndexDao sourceIndexDao;

    @Test
    public void getIndexTest() {
        String tableName = "user";
        IndexDO indexDO = destinationIndexDao.getIndexByTableName(tableName);
        System.out.println(indexDO);
        indexDO = sourceIndexDao.getIndexByTableName(tableName);
        System.out.println(indexDO);
    }

}
