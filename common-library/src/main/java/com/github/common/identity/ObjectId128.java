package com.github.common.identity;

import com.github.common.base.HexUtil;
import com.github.common.concurrent.ConcurrentAtomicInteger;
import com.github.jvm.util.UnsafeUtils;
import com.github.util.SystemClock;
import io.netty.buffer.ByteBuf;
import sun.misc.Unsafe;

import java.io.Serializable;
import java.net.NetworkInterface;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Objects;

import static sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/5/14.
 */
public final class ObjectId128 implements Serializable {

    private static final long serialVersionUID = -6453295018947782081L;

    private static final int LOW_ORDER_THREE_BYTES = 0x00FFFFFF;

    private static final int MACHINE_IDENTIFIER;
    private static final short PROCESS_IDENTIFIER;
    private static final long UNION_HIGH;

    private static final ConcurrentAtomicInteger NEXT_COUNTER = new ConcurrentAtomicInteger(new SecureRandom().nextInt());

    public final long timestamp;
    public final long union;

    /**
     * Gets a new object id.
     *
     * @return the new id
     */
    public static ObjectId128 next() {
        int next = NEXT_COUNTER.next() & LOW_ORDER_THREE_BYTES;
        long union = UNION_HIGH | next;

        return new ObjectId128(SystemClock.fast().mills(), union);
    }

    public ObjectId128(final long timestamp, final long union) {
        this.timestamp = timestamp;
        this.union = union;
    }

    public ObjectId128(final ByteBuf buffer) {
        Objects.requireNonNull(buffer, "buffer");

        timestamp = buffer.readLong();
        union = buffer.readLong();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getUnion() {
        return union;
    }

    public void writeTo(final ByteBuf buffer) {
        buffer.writeLong(timestamp);
        buffer.writeLong(union);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
        result = prime * result + (int) (union ^ (union >>> 32));
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
        ObjectId128 other = (ObjectId128) obj;
        if (timestamp != other.timestamp)
            return false;
        if (union != other.union)
            return false;
        return true;
    }

    /**
     * 128位编码, 长度为32
     *
     * @return
     */
    public String toHexString() {
        byte[] bytes = new byte[32];

        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 0, HexUtil.byteToHexLE(timestamp >>> 56));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 2, HexUtil.byteToHexLE(timestamp >>> 48));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 4, HexUtil.byteToHexLE(timestamp >>> 40));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 6, HexUtil.byteToHexLE(timestamp >>> 32));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 8, HexUtil.byteToHexLE(timestamp >>> 24));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 10, HexUtil.byteToHexLE(timestamp >>> 16));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 12, HexUtil.byteToHexLE(timestamp >>> 8));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 14, HexUtil.byteToHexLE(timestamp));

        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 16, HexUtil.byteToHexLE(union >>> 56));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 18, HexUtil.byteToHexLE(union >>> 48));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 20, HexUtil.byteToHexLE(union >>> 40));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 22, HexUtil.byteToHexLE(union >>> 32));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 24, HexUtil.byteToHexLE(union >>> 24));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 26, HexUtil.byteToHexLE(union >>> 16));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 28, HexUtil.byteToHexLE(union >>> 8));
        unsafe().putShort(bytes, ARRAY_BYTE_BASE_OFFSET + 30, HexUtil.byteToHexLE(union));

        return new String(bytes);
    }

    private Unsafe unsafe() {
        return UnsafeUtils.unsafe();
    }

    @Override
    public String toString() {
        return toHexString();
    }

    static {
        if (ByteOrder.nativeOrder() != ByteOrder.LITTLE_ENDIAN) {
            throw new Error("only support little-endian!");
        }

        try {
            MACHINE_IDENTIFIER = createMachineIdentifier();
            PROCESS_IDENTIFIER = createProcessIdentifier();

            UNION_HIGH = ((MACHINE_IDENTIFIER & 0xFF_FF_FFL) << 40) | ((PROCESS_IDENTIFIER & 0xFF_FFL) << 24);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static int createMachineIdentifier() {
        // build a 2-byte machine piece based on NICs info
        int machinePiece;
        try {
            StringBuilder sb = new StringBuilder();
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface ni = e.nextElement();
                sb.append(ni.toString());
                byte[] mac = ni.getHardwareAddress();
                if (mac != null) {
                    ByteBuffer bb = ByteBuffer.wrap(mac);
                    try {
                        sb.append(bb.getChar());
                        sb.append(bb.getChar());
                        sb.append(bb.getChar());
                    } catch (BufferUnderflowException shortHardwareAddressException) { // NOPMD
                        // mac with less than 6 bytes. continue
                    }
                }
            }
            machinePiece = sb.toString().hashCode();
        } catch (Throwable t) {
            // exception sometimes happens with IBM JVM, use random
            machinePiece = (new SecureRandom().nextInt());
            System.err.println("Failed to get machine identifier from network interface, using random number instead "
                    + Arrays.toString(t.getStackTrace()));
        }
        machinePiece = machinePiece & LOW_ORDER_THREE_BYTES;
        return machinePiece;
    }

    // Creates the process identifier. This does not have to be unique per class
    // loader because
    // NEXT_COUNTER will provide the uniqueness.
    private static short createProcessIdentifier() {
        short processId;
        try {
            String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
            if (processName.contains("@")) {
                processId = (short) Integer.parseInt(processName.substring(0, processName.indexOf('@')));
            } else {
                processId = (short) java.lang.management.ManagementFactory.getRuntimeMXBean().getName().hashCode();
            }

        } catch (Throwable t) {
            processId = (short) new SecureRandom().nextInt();

            System.err.println("Failed to get process identifier from JMX, using random number instead "
                    + Arrays.toString(t.getStackTrace()));
        }

        return processId;
    }
}
