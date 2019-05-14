package com.github.common.base;

import java.nio.ByteOrder;

/**
 * @Author: alex
 * @Description: 查表 + 批量操作，是 Netty 和 Guava 实现的 2x~5x
 * @Date: created in 2018/5/14.
 */
public class HexUtil {
    private static final byte[] EMPTY_BYTES = new byte[0];

    private static final short[] HEX_TABLE = new short[256];
    private static final short[] HEX_TABLE_LE = new short[256];
    private static final short[] UPPER_HEX_TABLE = new short[256];
    private static final short[] UPPER_HEX_TABLE_LE = new short[256];

    private static final byte[] BYTE_TABLE;
    private static final byte[] BYTE_TABLE_LE;

    static {
        if (ByteOrder.nativeOrder() != ByteOrder.LITTLE_ENDIAN) {
            throw new Error("only support little-endian!");
        }

        final char[] DIGITS = "0123456789abcdef".toCharArray();
        final char[] UPPER_DIGITS = "0123456789ABCDEF".toCharArray();

        for (int i = 0; i < 256; i++) {
            int high = DIGITS[(0xF0 & i) >>> 4];
            int low = DIGITS[0x0F & i];

            HEX_TABLE[i] = (short) (high << 8 | low);
            HEX_TABLE_LE[i] = (short) (low << 8 | high);

            high = UPPER_DIGITS[(0xF0 & i) >>> 4];
            low = UPPER_DIGITS[0x0F & i];

            UPPER_HEX_TABLE[i] = (short) (high << 8 | low);
            UPPER_HEX_TABLE_LE[i] = (short) (low << 8 | high);
        }

        BYTE_TABLE = new byte[HEX_TABLE[255] + 1];
        for (int i = 0; i < 256; i++) {
            BYTE_TABLE[HEX_TABLE[i]] = (byte) i;
            BYTE_TABLE[UPPER_HEX_TABLE[i]] = (byte) i;
        }

        BYTE_TABLE_LE = new byte[HEX_TABLE_LE[255] + 1];
        for (int i = 0; i < 256; i++) {
            BYTE_TABLE_LE[HEX_TABLE_LE[i]] = (byte) i;
            BYTE_TABLE_LE[UPPER_HEX_TABLE_LE[i]] = (byte) i;
        }
    }

    public static short byteToHexLE(int b) {
        return HEX_TABLE_LE[b & 0xFF];
    }

    public static short byteToHexLE(byte b) {
        return HEX_TABLE_LE[b & 0xFF];
    }

    public static short byteToHexLE(long b) {
        return HEX_TABLE_LE[((int) b) & 0xFF];
    }

    public static short byteToHex(byte b) {
        return HEX_TABLE[b & 0xFF];
    }
    public static byte hexToByteLE(short hex) {
        return BYTE_TABLE_LE[hex];
    }
}
