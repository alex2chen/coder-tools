package com.github.common.collect;

import com.github.common.base.map.FastMap;
import com.github.common.base.map.KeyNamespace;
import com.github.common.base.map.MapKey;
import com.github.common.base.map.support.DefaultFastMap;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

/**
 * @Author: alex
 * @Description: 性能提升约30%
 * putHashMap cost:6.131 ms
 * putFastMap cost:2.302 ms
 * getHashMap cost:2.544 ms
 * getFastMap cost:970.1 μs
 * @Date: created in 2018/6/6.
 */
public class Map_Benchmark {
    private HashMap<String, String> hashMap = Maps.newHashMap();
    private static final int SIZE = 10000;
    private KeyNamespace keyNamespace = KeyNamespace.createNamespace("order");
    private FastMap<String> fastMap = new DefaultFastMap(keyNamespace, SIZE);

    private MapKey[] initKeys = new MapKey[SIZE];

    @Before
    public void initFast() {
        //性能提升在于初始化阶段完成，否则性能提升不大
        for (int i = 0; i < SIZE; i++) {
            initKeys[i] = new MapKey(keyNamespace, "" + i);
        }
    }

    @Test
    public void putGetMap() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < SIZE; i++) {
            hashMap.put("" + i, "a" + i);
        }
        System.out.printf("putHashMap cost:%s \n", stopwatch);
//        stopwatch = Stopwatch.createStarted();
//        for (int i = 0; i < SIZE; i++) {
//            fastMap.put("" + i, "a" + i);
//        }
//        System.out.printf("direct putFastMap cost:%s\n", stopwatch);

        stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < SIZE; i++) {
            fastMap.put(initKeys[i], "a" + i);
        }
        System.out.printf("putFastMap cost:%s\n", stopwatch);

        stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < SIZE; i++) {
            hashMap.get("" + i);
        }
        System.out.printf("getHashMap cost:%s\n", stopwatch);
//        stopwatch = Stopwatch.createStarted();
//        for (int i = 0; i < SIZE; i++) {
//            fastMap.get(keyNamespace.getOrCreate("" + i));
//        }
//        System.out.printf("direct getFastMap cost:%s\n", stopwatch);
        stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < SIZE; i++) {
            fastMap.get(initKeys[i]);
        }
        System.out.printf("getFastMap cost:%s\n", stopwatch);
    }

}
