package com.github.common;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/5/5
 */
public class TypeToken_test {
    /**
     * 泛型运行时类型擦除的问题
     */
    @Test
    public void typeRemove() {
        ArrayList<String> stringList = Lists.newArrayList();
        ArrayList<Integer> intList = Lists.newArrayList();
        Assert.assertTrue(intList.getClass() == ArrayList.class);
        Assert.assertTrue(stringList.getClass().isAssignableFrom(intList.getClass()));
        //but,TypeToken is @Beta
        TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>() {};
        TypeToken<?> genericTypeToken = typeToken.resolveType(ArrayList.class.getTypeParameters()[0]);
        Assert.assertTrue(genericTypeToken.getType() == String.class);
        //全部通过
    }
}
