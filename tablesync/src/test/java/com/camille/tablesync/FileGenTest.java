package com.camille.tablesync;

import com.camille.tablesync.utils.FileGenUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @FileName: FileGenTest.java
 * @Description: FileGenTest.java类说明
 * @Author: camille
 * @Date: 2021/4/3 15:57
 */
@SpringBootTest
public class FileGenTest {


    @Test
    public void generateFileTest() {
        // 生成txt文件
        List<String> source = new ArrayList<>();
        source.add("aaa");
        source.add("bbb");
        source.add("ccc");
        source.add("ddd");
        FileGenUtils.fileGenByList(source);
    }

}
