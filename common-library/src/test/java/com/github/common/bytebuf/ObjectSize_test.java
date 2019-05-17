package com.github.common.bytebuf;

import com.github.jvm.util.UnsafeUtils;
import com.javamex.classmexer.MemoryUtil;
import org.junit.Test;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2016/5/17.
 * oops：ordinary object pointers，普通对象引用指针
 * 对象占用内存大小，HotSpot VM的自动内存管理系统要求对象大小必须是8字节的整数倍，对齐填充没有特别的含义，它仅仅起着占位符的作用。
 * -XX:+UseCompressedOops
 * JDK 8下默认为启用,启用对象的指针压缩，节约内存占用的大小
 * -XX:+CompactFields
 * JDK 8下默认为启用，分配一个非static的字段在前面字段缝隙中，提高内存的利用率
 * -XX:FieldsAllocationStyle=1
 * JDK 8下默认值为‘1’，实例对象中有效信息的存储顺序
 * 0：先放入oops，然后在放入基本变量类型（顺序：longs/doubles、ints/floats、shorts/chars、bytes/booleans），如果longs/doubles都有按定义的顺序
 * 1：先放入基本变量类型（顺序：longs/doubles、ints/floats、shorts/chars、bytes/booleans），然后放入oops
 * 2：oops和基本变量类型交叉存储
 * 1.一般对象头大小
 * 32 位系统	        64 位系统(+UseCompressedOops)	            64 位系统(-UseCompressedOops)
 * Mark Word		4 bytes		8 bytes							8 bytes
 * Class Pointer	4 bytes		4 bytes							8 bytes
 * 总计			    8 bytes		12 bytes						16 bytes
 * 2.数组对象对象头大小
 * 32 位系统	         64 位系统(+UseCompressedOops)	64 位系统(-UseCompressedOops)
 * Mark Word		4 bytes		8 bytes							8 bytes
 * Class Pointer	4 bytes		4 bytes							8 bytes
 * Length			4 bytes		4 bytes							2 bytes
 * 总计			    12 bytes	16 bytes						20 bytes
 * 基本数据类型（32/64位,不区分压缩）
 * double	8 bytes
 * long	    8 bytes
 * float	4 bytes
 * int		4 bytes
 * char	    2 bytes
 * short	2 bytes
 * byte	    1 bytes
 * boolean  1 bytes
 * oops(ordinary object pointers)	4 bytes
 * 包装类型
 * +useCompressedOops	-useCompressedOops
 * Byte, Boolean		16 bytes			24 bytes
 * Short, Character 	16 bytes			24 bytes
 * Integer, Float		16 bytes			24 bytes
 * Long, Double		    24 bytes			24 bytes
 */
public class ObjectSize_test {
    /**
     * 启动参数中添加: -javaagent:D:\Program\repository\classmexer\classmexer\0.03\classmexer-0.03.jar
     */
    @Test
    public void showObjSize() {
        LongInstace v = new LongInstace();
        //24=12+8+2+2=对象头+long+byte+padding(2)，不压缩的话就是16+8+2...
        System.out.printf("shallow size:    %s.byte\n", MemoryUtil.memoryUsageOf(v));
        System.out.printf("retained size:   %s.byte\n", MemoryUtil.deepMemoryUsageOf(v));

        LongInstace2 v2 = new LongInstace2();
        //16=12+4=对象头+oops
        System.out.printf("shallow size:    %s.byte\n", MemoryUtil.memoryUsageOf(v2));
        //40=16+24=LongInstace2+Long
        System.out.printf("retained size:   %s.byte\n", MemoryUtil.deepMemoryUsageOf(v2));

        IntegerInstace1 v3 = new IntegerInstace1();
        //16=12+4=对象头+oops
        System.out.printf("shallow size:    %s.byte\n", MemoryUtil.memoryUsageOf(v3));
        //32=16+16=LongInstace3+Integer，有压缩
        System.out.printf("retained size:   %s.byte\n", MemoryUtil.deepMemoryUsageOf(v3));

        LongInstace[]vs=new LongInstace[2];
        vs[0]=v;
        System.out.printf("shallow size:    %s.byte\n", MemoryUtil.memoryUsageOf(vs));
        System.out.printf("retained size:   %s.byte\n", MemoryUtil.deepMemoryUsageOf(vs));

        System.out.printf("first item offset : %s,per item size: %s \n",UnsafeUtils.unsafe().arrayBaseOffset(vs.getClass()),UnsafeUtils.unsafe().arrayIndexScale(LongInstace[].class));

        ComplexInstance obj = new ComplexInstance();
        //48=12+1+2+2+4+4+8+8+padding(7)
        System.out.println("ComplexInstance:" + MemoryUtil.memoryUsageOf(obj));
    }

    @Test
    public void showLayout() throws NoSuchFieldException {
        /**
         * 无填充
         * 内存布局：对象头(12) + oops(4)
         */
        long offset = UnsafeUtils.unsafe().objectFieldOffset(IntegerInstace1.class.getDeclaredField("value"));
        //12=对象头(12) 开始
        System.out.printf("IntegerInstace1.value(Integer):  %s \n", offset);
        /**
         * 有填充
         * 规则：64位系统中CPU一次读操作可读取64bit（8 bytes）的数据，读取属性long不能读取2次(如果从对象头开始就会发生)
         */
        offset = UnsafeUtils.unsafe().objectFieldOffset(LongInstace1.class.getDeclaredField("value"));
        //16=对象头(12)+padding(4)
        System.out.printf("LongInstace1.value(long):    %s \n", offset);
        offset = UnsafeUtils.unsafe().objectFieldOffset(LongInstace.class.getDeclaredField("value2"));
        //12=对象头(12)    -XX:+CompactFields生效
        System.out.printf("LongInstace.value2(byte):    %s \n", offset);
        offset = UnsafeUtils.unsafe().objectFieldOffset(LongInstace.class.getDeclaredField("value3"));
        //13=对象头(12)+value2     -XX:+CompactFields生效
        System.out.printf("LongInstace.value3(char):    %s \n", offset);
        /**
         * 内部有多个成员变量时，布局按-XX:FieldsAllocationStyle=1（longs/doubles、ints/floats、shorts/chars、bytes/booleans）
         * double=24=对象头(12)+int(4)+long(8)，double和long是有变量定义的顺序决定的，不行你可以试试
         * 48=对象头(12)+int(4)+long(8)+double(8)+float(4)+char(2)+short(2)+boolean(1)+padding(7)
         */
        offset = UnsafeUtils.unsafe().objectFieldOffset(ComplexInstance.class.getDeclaredField("bool"));
        System.out.printf("ComplexInstance.bool(boolean):   %s \n", offset);//40
        offset = UnsafeUtils.unsafe().objectFieldOffset(ComplexInstance.class.getDeclaredField("cha"));
        System.out.printf("ComplexInstance.cha(char):   %s \n", offset);//36
        offset = UnsafeUtils.unsafe().objectFieldOffset(ComplexInstance.class.getDeclaredField("sho"));
        System.out.printf("ComplexInstance.sho(short):   %s \n", offset);//38
        offset = UnsafeUtils.unsafe().objectFieldOffset(ComplexInstance.class.getDeclaredField("in"));
        System.out.printf("ComplexInstance.in(int):   %s \n", offset);//12
        offset = UnsafeUtils.unsafe().objectFieldOffset(ComplexInstance.class.getDeclaredField("flo"));
        System.out.printf("ComplexInstance.flo(float):   %s \n", offset);//32
        offset = UnsafeUtils.unsafe().objectFieldOffset(ComplexInstance.class.getDeclaredField("lon"));
        System.out.printf("ComplexInstance.lon(long):   %s \n", offset);//16
        offset = UnsafeUtils.unsafe().objectFieldOffset(ComplexInstance.class.getDeclaredField("dou"));
        System.out.printf("ComplexInstance.dou(double):   %s \n", offset);//24
    }

    public final static class LongInstace {
        protected long value = 0L;
        byte value2;
        boolean value3;
    }

    public final static class LongInstace1 {
        protected long value = 0L;
    }

    public final static class LongInstace2 {
        Long value = 0L;
    }

    public final static class IntegerInstace1 {
        Integer value = 0;
    }

    public final static class ComplexInstance {
        boolean bool;

        char cha;
        short sho;

        int in;
        float flo;

        long lon;
        double dou;
    }
}
