package com.camille.tablesync.utils;

import lombok.Data;

/**
 * @FileName: Pair.java
 * @Description: Pair.java类说明
 * @Author: camille
 * @Date: 2021/3/21 14:16
 */
@Data
public class Pair<T> {

    T sourceData;
    T destinationData;

    public Pair(T sourceData, T destinationData) {
        this.sourceData = sourceData;
        this.destinationData = destinationData;
    }

}
