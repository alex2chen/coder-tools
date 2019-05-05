package com.github.jvm.util.collection;

import com.github.jvm.base.TimeTestUnit;
import com.github.jvm.util.UnsafeUtils;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * @Author: alex
 * @Description: 最快的数组
 * @Date: created in 2018/4/29.
 */
public class Array_unit extends TimeTestUnit {
    private final int length = 1024;
    private byte[] bytes = new byte[1024];
    private byte[] bytes2 = new byte[1024];
    private ByteBuffer byteBuffe = ByteBuffer.allocate(length);
    private ByteBuffer directByteBuffe = ByteBuffer.allocateDirect(length);//非堆内存

    private byte loopReadByteBytesForeach() {
        byte value = 0;
        for (byte b : bytes) {
            value = b;
        }
        return value;
    }
    private byte loopReadByteBytes() {
        byte value = 0;
        for (int i = 0; i < length; i++) {
            value = bytes[i];
        }
        return value;
    }

    private byte loopReadByteByteBuffer() {
        byte value = 0;
        byteBuffe.position(0);
        while (byteBuffe.hasRemaining()) {
            value = byteBuffe.get();
        }
        return value;
    }

    /**
     * loopReadByteBytesForeach:3.319 ms
     * loopReadByteBytes:13.64 ms
     * loopReadByteByteBuffer:17.96 ms
     */
    @Test
    public void calReadConsume() {
        int count = 10000;
        runTimes(count, "loopReadByteBytes", x -> loopReadByteBytes());
        runTimes(count, "loopReadByteBytesForeach", x -> loopReadByteBytesForeach());
        runTimes(count, "loopReadByteByteBuffer", x -> loopReadByteByteBuffer());

    }

    /**
     * loopSetBytes:8.119 ms
     * unsafeCopyMemorySetByteBytes:2.244 ms
     * arraycopyByteBytes:857.5 μs
     * loopSetByteBuffer:19.10 ms
     * loopSetDirectByteBuffer:14.71 ms
     * batchSetByteBuffer:1.816 ms
     * batchSetDirectByteBuffer:4.612 ms
     */
    @Test
    public void calWriteConsume() {
        int count = 10000;
        runTimes(count, "loopSetBytes", x -> loopSetBytes());
        runTimes(count, "unsafeCopyMemorySetByteBytes", x -> unsafeCopyMemorySetByteBytes());
        runTimes(count, "arraycopyByteBytes", x -> arraycopyByteBytes());
        runTimes(count, "loopSetByteBuffer", x -> loopSetByteBuffer());
        runTimes(count, "loopSetDirectByteBuffer", x -> loopSetDirectByteBuffer());
        runTimes(count, "batchSetByteBuffer", x -> batchSetByteBuffer());
        runTimes(count, "batchSetDirectByteBuffer", x -> batchSetDirectByteBuffer());
    }

    public void loopSetBytes() {
        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) i;
        }
    }

    public void unsafeCopyMemorySetByteBytes() {
        UnsafeUtils.unsafe().copyMemory(bytes, 0, bytes2, 0, length);
    }

    public void arraycopyByteBytes() {
        System.arraycopy(bytes, 0, bytes2, 0, length);
    }

    public void loopSetByteBuffer() {
        byteBuffe.clear();
        for (int i = 0; i < length; i++) {
            byteBuffe.put((byte) i);
        }
    }

    public void loopSetDirectByteBuffer() {
        directByteBuffe.clear();
        for (int i = 0; i < length; i++) {
            directByteBuffe.put((byte) i);
        }
    }

    public void batchSetByteBuffer() {
        byteBuffe.clear();
        byteBuffe.put(bytes);
    }

    public void batchSetDirectByteBuffer() {
        directByteBuffe.clear();
        directByteBuffe.put(bytes);
    }

}
