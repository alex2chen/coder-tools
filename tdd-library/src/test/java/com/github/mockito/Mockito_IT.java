package com.github.mockito;

;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/11/11
 */
public class Mockito_IT {
    /**
     * 1.验证行为
     */
    @Test
    public void verify_behaviour() {
        List mock = mock(List.class);
        mock.add(1);
        mock.clear();
        //验证add(1)和clear()行为是否发生
        verify(mock).add(1);
        verify(mock).clear();
    }

    /**
     * 2.验证期望的结果
     */
    @Test
    public void verify_expData() {
        Iterator iterator = mock(Iterator.class);
        //预设当iterator调用next()时第一次返回hello，第n次都返回world
        when(iterator.next()).thenReturn("hello").thenReturn("world");
        String result = iterator.next() + " " + iterator.next() + " " + iterator.next();
        assertEquals("hello world world", result);
    }

    /**
     * 3.参数匹配
     */
    @Test
    public void with_arguments() {
        Comparable comparable = mock(Comparable.class);
        //预设根据不同的参数返回不同的结果
        when(comparable.compareTo("Test")).thenReturn(1);
        when(comparable.compareTo("oms")).thenReturn(2);
        assertEquals(1, comparable.compareTo("Test"));
        assertEquals(2, comparable.compareTo("oms"));
        //对于没有预设的情况会返回默认值
        assertEquals(0, comparable.compareTo("Not stu"));
    }

    @Test
    public void with_unspecified_arguments() {
        List list = mock(List.class);
        when(list.get(anyInt())).thenReturn(1);
        when(list.contains(argThat(new IsValid()))).thenReturn(true);
        assertEquals(1, list.get(1));
        assertEquals(1, list.get(999));
        assertTrue(list.contains(1));
        assertTrue(!list.contains(3));
        //assertTrue(list.contains("abc"));
    }

    /**
     * 4.验证确切的调用次数
     */
    @Test
    public void verifying_number_of_invocations() {
        List list = mock(List.class);
        list.add(1);
        list.add(2);
        list.add(2);
        list.add(3);
        list.add(3);
        list.add(3);
        //验证是否被调用一次，等效于下面的times(1)
        //verify(list).add(1);
        verify(list, times(1)).add(1);
        //验证是否被调用2次
        verify(list, times(2)).add(2);
        //验证是否被调用3次
        verify(list, times(3)).add(3);
        //验证是否从未被调用过
        verify(list, never()).add(4);
        //验证至少调用一次
        verify(list, atLeastOnce()).add(1);
        //验证至少调用2次
        verify(list, atLeast(2)).add(2);
        //验证至多调用3次
        verify(list, atMost(3)).add(3);
    }

    /**
     * 5.模拟方法体抛出异常
     *
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void verify_thenThrow() throws IOException {
        OutputStream outputStream = mock(OutputStream.class);
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        //预设当流关闭时抛出异常
        doThrow(new IOException()).when(outputStream).close();
        outputStream.close();
    }

    @Test(expected = RuntimeException.class)
    public void doThrow_when() {
        List list = mock(List.class);
        doThrow(new RuntimeException()).when(list).add(1);
        list.add(1);
//        list.add(2);
    }

    /**
     * 6.验证执行顺序
     */
    @Test
    public void verification_in_order() {
        List list = mock(List.class);
        List list2 = mock(List.class);
        list.add(1);
        list2.add("hello");
        list.add(2);
        list2.add("world");
        //将需要排序的mock对象放入InOrder
        InOrder inOrder = inOrder(list, list2);
        //下面的代码不能颠倒顺序，验证执行顺序
        inOrder.verify(list).add(1);
        inOrder.verify(list2).add("hello");
        inOrder.verify(list).add(2);
        inOrder.verify(list2).add("world");
    }

    @Test
    public void staticMethod_test() {
        mock(StrUtil.class);
        when(StrUtil.split("")).thenReturn("bbs");
        assertEquals("bbs", StrUtil.split(""));
    }

    public static class StrSplit {
        /**
         * @return
         */
        public static String split() {
            return "a,b";
        }
    }

    public static class StrUtil {
        /**
         * @param input
         * @return
         */
        public static String split(String input) {
            return StrSplit.split();
        }
    }

    public static class IsValid extends ArgumentMatcher<List> {
        /**
         * @param num
         * @return
         */
        @Override
        public boolean matches(Object num) {
            if (num instanceof Integer) {
                return (Integer) num == 1 || (Integer) num == 2;
            } else {
                return false;
            }
        }
    }
}

