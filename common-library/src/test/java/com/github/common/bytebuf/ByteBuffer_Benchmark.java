package com.github.common.bytebuf;

import com.github.jvm.util.UnsafeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.util.internal.PlatformDependent;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

/**
 * @Author: alex
 * @Description: JMH参阅: HexBenchmark
 * @Date: created in 2018/5/14.
 * 性能大比拼:
 * Benchmark                                            Mode  Cnt      Score   Units
 * ByteBuffer_Benchmark.writeByteBytes                 thrpt    3  13837.039  ops/ms
 * ByteBuffer_Benchmark.writeByteDirectByteBuffer      thrpt    3  10461.619  ops/ms
 * ByteBuffer_Benchmark.writeByteHeapByteBuffer        thrpt    3  10649.460  ops/ms
 * ByteBuffer_Benchmark.writeByteNettyByteBuf          thrpt    3   2950.805  ops/ms
 * ByteBuffer_Benchmark.writeByteUnsafe                thrpt    3   9621.168  ops/ms
 *
 * ByteBuffer_Benchmark.writeIntBytes                  thrpt    3  12845.335  ops/ms
 * ByteBuffer_Benchmark.writeIntDirectByteBuffer       thrpt    3  11678.054  ops/ms
 * ByteBuffer_Benchmark.writeIntHeapByteBuffer         thrpt    3  10880.551  ops/ms
 * ByteBuffer_Benchmark.writeIntNettyByteBuf           thrpt    3  10218.105  ops/ms
 * ByteBuffer_Benchmark.writeIntToUnsafe               thrpt    3   9762.825  ops/ms
 *
 * ByteBuffer_Benchmark.writeLongBytes                 thrpt    3  13706.526  ops/ms
 * ByteBuffer_Benchmark.writeLongDirectAndCopyToNetty  thrpt    3  14740.679  ops/ms
 * ByteBuffer_Benchmark.writeLongDirectByteBuffer      thrpt    3  11312.111  ops/ms
 * ByteBuffer_Benchmark.writeLongHeapByteBuffer        thrpt    3  10173.872  ops/ms
 * ByteBuffer_Benchmark.writeLongNettyByteBuf          thrpt    3  10664.618  ops/ms
 * ByteBuffer_Benchmark.writeLongToUnsafe              thrpt    3  10297.256  ops/ms
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
@Threads(4)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ByteBuffer_Benchmark {
    private final byte[] bytes = new byte[1024];
    private final ByteBuffer heapByteBuffer = ByteBuffer.allocate(1024);
    private final ByteBuffer directByteBuffer = ByteBuffer.allocateDirect(1024);
    //默认为Pool directBuffer
    private final ByteBuf nettyByteBuf = PooledByteBufAllocator.DEFAULT.buffer(1024, 1024);
    private final long unsafeOffset = UnsafeUtils.unsafe().allocateMemory(1024);

    @Test
    public void dispalyPoolBufferType() {
        System.out.println(PlatformDependent.directBufferPreferred());
    }

    @Benchmark
    @Fork(1)
    public void writeByteBytes() {
        for (int i = 0; i < 1024; i++) {
            bytes[i] = (byte) 1;
        }
    }

    @Benchmark
    @Fork(1)
    public void writeIntBytes() {
        for (int i = 0; i < 1024; i += 4) {
            bytes[i] = 1;
        }
    }

    @Benchmark
    @Fork(1)
    public void writeLongBytes() {
        for (int i = 0; i < 1024; i += 8) {
            bytes[i] = 1;
        }
    }

    @Benchmark
    @Fork(1)
    public void writeByteHeapByteBuffer() {
        for (int i = 0; i < 1024; i++) {
            heapByteBuffer.put(i, (byte) 1);
        }
    }

    @Benchmark
    @Fork(1)
    public void writeIntHeapByteBuffer() {
        for (int i = 0; i < 1024; i += 4) {
            heapByteBuffer.putInt(i, 1);
        }
    }

    @Benchmark
    @Fork(1)
    public void writeLongHeapByteBuffer() {
        for (int i = 0; i < 1024; i += 8) {
            heapByteBuffer.putLong(i, 1L);
        }
    }

    @Benchmark
    @Fork(1)
    public void writeByteDirectByteBuffer() {
        for (int i = 0; i < 1024; i++) {
            directByteBuffer.put(i, (byte) 1);
        }
    }

    @Benchmark
    @Fork(1)
    public void writeIntDirectByteBuffer() {
        for (int i = 0; i < 1024; i += 4) {
            directByteBuffer.putInt(i, 1);
        }
    }

    @Benchmark
    @Fork(1)
    public void writeLongDirectByteBuffer() {
        for (int i = 0; i < 1024; i += 8) {
            directByteBuffer.putLong(i, 1L);
        }
    }

    @Benchmark
    @Fork(1)
    public void writeByteUnsafe() {
        for (int i = 0; i < 1024; i++) {
            UnsafeUtils.unsafe().putByte(unsafeOffset + i, (byte) 1);
        }
    }

    @Benchmark
    @Fork(1)
    public void writeIntToUnsafe() {
        for (int i = 0; i < 1024; i += 4) {
            UnsafeUtils.unsafe().putInt(unsafeOffset + i, 1);
        }
    }

    @Benchmark
    @Fork(1)
    public void writeLongToUnsafe() {
        for (int i = 0; i < 1024; i += 8) {
            UnsafeUtils.unsafe().putLong(unsafeOffset + i, 1L);
        }
    }

    @Benchmark
    @Fork(1)
    public void writeByteNettyByteBuf() {
        for (int i = 0; i < 1024; i++) {
            nettyByteBuf.setByte(i, 1);
        }
    }

    @Benchmark
    @Fork(1)
    public void writeIntNettyByteBuf() {
        for (int i = 0; i < 1024; i += 4) {
            nettyByteBuf.setInt(i, 1);
        }
    }

    @Benchmark
    @Fork(1)
    public void writeLongNettyByteBuf() {
        for (int i = 0; i < 1024; i += 8) {
            nettyByteBuf.setLong(i, 1L);
        }
    }

    @Benchmark
    @Fork(1)
    public void writeByteDirectAndCopyToNetty() {
        for (int i = 0; i < 1024; i++) {
            directByteBuffer.put(i, (byte) 1);
        }
        nettyByteBuf.setBytes(0, directByteBuffer);
    }

    @Benchmark
    @Fork(1)
    public void writeIntDirectAndCopyToNetty() {
        for (int i = 0; i < 1024; i += 4) {
            directByteBuffer.putInt(i, 1);
        }
        nettyByteBuf.setBytes(0, directByteBuffer);
    }

    @Benchmark
    @Fork(1)
    public void writeLongDirectAndCopyToNetty() {
        for (int i = 0; i < 1024; i += 8) {
            directByteBuffer.putLong(i, 1L);
        }
        nettyByteBuf.setBytes(0, directByteBuffer);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ByteBuffer_Benchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
