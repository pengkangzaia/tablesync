package com.camille.tablesync.entity;

import lombok.Data;

/**
 * @FileName: Field.java
 * @Description: Field.java类说明
 * @Author: kang.peng
 * @Date: 2021/3/19 17:43
 */
@Data
public class Field {

    private String fieldName;
    private String fieldType;
    private String fieldAllowNull;
    private String fieldKey;
    private String fieldDefault;
    private String fieldExtra;

}
