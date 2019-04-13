package com.github.common.collect;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.junit.Test;

/**
 * @author alex.chen
 * @Description:
 * @date 2017/4/14
 */
public class HashBiMap_unit {
    @Test
    public void getByValue(){
        //双向map,可以通过key得到value，也可以通过value得到key
        BiMap<Integer, String> biMap = HashBiMap.create();

        biMap.put(1, "hello");
        biMap.put(2, "helloa");
        biMap.put(3, "world");
        biMap.put(4, "worldb");
        biMap.put(5, "my");
        biMap.put(6, "myc");

        int value = biMap.inverse().get("my");
        System.out.println("my --" + value);
    }
}
