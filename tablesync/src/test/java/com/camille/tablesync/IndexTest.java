package com.camille.tablesync;

import com.camille.tablesync.dao.destination.DestinationIndexDao;
import com.camille.tablesync.dao.source.SourceIndexDao;
import com.camille.tablesync.entity.IndexDO;
import com.camille.tablesync.service.IndexService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.util.List;

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

    @Autowired
    private IndexService indexService;

    @Test
    public void getIndexTest() {
        String tableName = "user";
        List<IndexDO> indexes = destinationIndexDao.getIndexByTableName(tableName);
        if (!CollectionUtils.isEmpty(indexes)) {
            for (IndexDO index : indexes) {
                System.out.println(index);
            }
        }
        indexes = sourceIndexDao.getIndexByTableName(tableName);
        if (!CollectionUtils.isEmpty(indexes)) {
            for (IndexDO index : indexes) {
                System.out.println(index);
            }
        }
    }

    @Test
    public void getDiffIndexTest() {
        String tableName = "user";
        String indexDiffSql = indexService.getIndexDiffSql(tableName);
        System.out.println(indexDiffSql);
    }

}
