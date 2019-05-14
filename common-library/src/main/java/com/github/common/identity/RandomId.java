package com.github.common.identity;

import com.github.common.base.HexUtil;
import com.github.jvm.util.UnsafeUtils;
import com.github.util.SystemClock;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.ThreadLocalRandom;
import sun.misc.Unsafe;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.LocalDateTime;
import java.util.Objects;

import static java.time.ZoneOffset.UTC;
import static sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/5/14.
 */
public final class RandomId implements Serializable {

    private static final long serialVersionUID = -2951994548246899462L;

    private static final long HEX32_PADDING = ByteBuffer.wrap("0000000000000000".getBytes()).getLong();
    public final static long OFFSET_SECONDS = LocalDateTime.parse("2018-01-01T00:00:00").toEpochSecond(UTC);

    public final int timestamp;
    public final long random;

    /**
     * Gets a new object id.
     *
     * @return the new id
     */
    public static RandomId next() {
        return new RandomId(currentSeconds(), ThreadLocalRandom.current().nextLong());
    }

    public RandomId(final int timestamp, final long random) {
        this.timestamp = timestamp;
        this.random = random;
    }

    public RandomId(final ByteBuf buffer) {
        Objects.requireNonNull(buffer, "buffer");

        timestamp = buffer.readInt();
        random = buffer.readLong();
    }

    public int getTimestamp() {
        return timestamp;
    }

    public long getRandom() {
        return random;
    }

    public void writeTo(final ByteBuf buffer) {
        buffer.writeInt(timestamp);
        buffer.writeLong(random);
    }

    public String toHexString() {
        // java9 下更高效
        byte[] bytes = new byte[24];

        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 0, HexUtil.byteToHexLE(timestamp >>> 24));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 2, HexUtil.byteToHexLE(timestamp >>> 16));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 4, HexUtil.byteToHexLE(timestamp >>> 8));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 6, HexUtil.byteToHexLE(timestamp));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 8, HexUtil.byteToHexLE(random >>> 56));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 10, HexUtil.byteToHexLE(random >>> 48));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 12, HexUtil.byteToHexLE(random >>> 40));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 14, HexUtil.byteToHexLE(random >>> 32));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 16, HexUtil.byteToHexLE(random >>> 24));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 18, HexUtil.byteToHexLE(random >>> 16));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 20, HexUtil.byteToHexLE(random >>> 8));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 22, HexUtil.byteToHexLE(random));

        return new String(bytes);
    }

    /**
     * 128位编码, 长度为32, 前8个字符用'0'填充
     *
     * @return
     */
    public String toHexString32() {
        byte[] bytes = new byte[32];

        unsafe().putLong(bytes, ARRAY_BYTE_BASE_OFFSET, HEX32_PADDING);
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 0 + 8, HexUtil.byteToHexLE(timestamp >>> 24));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 2 + 8, HexUtil.byteToHexLE(timestamp >>> 16));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 4 + 8, HexUtil.byteToHexLE(timestamp >>> 8));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 6 + 8, HexUtil.byteToHexLE(timestamp));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 8 + 8, HexUtil.byteToHexLE(random >>> 56));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 10 + 8, HexUtil.byteToHexLE(random >>> 48));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 12 + 8, HexUtil.byteToHexLE(random >>> 40));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 14 + 8, HexUtil.byteToHexLE(random >>> 32));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 16 + 8, HexUtil.byteToHexLE(random >>> 24));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 18 + 8, HexUtil.byteToHexLE(random >>> 16));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 20 + 8, HexUtil.byteToHexLE(random >>> 8));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 22 + 8, HexUtil.byteToHexLE(random));

        return new String(bytes);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (random ^ (random >>> 32));
        result = prime * result + timestamp;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        RandomId other = (RandomId) obj;
        if (random != other.random)
            return false;

        if (timestamp != other.timestamp)
            return false;

        return true;
    }

    private Unsafe unsafe() {
        return UnsafeUtils.unsafe();
    }

    @Override
    public String toString() {
        return toHexString();
    }

    private static int currentSeconds() {
        return (int) (SystemClock.fast().seconds() - OFFSET_SECONDS);
    }

    static {
        if (ByteOrder.nativeOrder() != ByteOrder.LITTLE_ENDIAN) {
            throw new Error("only support little-endian!");
        }
    }

}
