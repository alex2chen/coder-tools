package com.github.common.net;

import com.github.common.base.HexUtil;
import com.github.common.concurrent.AttachThreadUtil;
import com.github.jvm.util.UnsafeUtils;
import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import io.netty.util.internal.InternalThreadLocalMap;
import org.apache.commons.codec.net.URLCodec;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET;

/**
 * @Author: alex
 * @Description: JMH参阅: HexBenchmark
 * @Date: created in 2018/5/14.
 * Benchmark                                         Mode  Cnt     Score   Units
 * URLEncode_Benchmark.decodeByURLEncodeUtil        thrpt    3  3725.688  ops/ms
 * URLEncode_Benchmark.decodeByCommonCodecURLCodec  thrpt    3  3589.653  ops/ms
 * URLEncode_Benchmark.decodeByJavaURLEncoder       thrpt    3  1337.201  ops/ms
 *
 * URLEncode_Benchmark.encodeByGuavaUrlEscapers     thrpt    3  7332.821  ops/ms
 * URLEncode_Benchmark.encodeByURLEncodeUtil        thrpt    3  3939.404  ops/ms
 * URLEncode_Benchmark.encodeByCommonCodecURLCodec  thrpt    3  2634.708  ops/ms
 * URLEncode_Benchmark.encodeByJavaURLEncoder       thrpt    3  2623.227  ops/ms
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
@Threads(4)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class URLEncode_Benchmark {
    private Escaper urlEscaper = UrlEscapers.urlPathSegmentEscaper();
    private static final URLCodec codecer = new URLCodec("UTF-8");
    private final String str = "2018 年 4 月宣布开源开源 SOFABoot、SOFARPC、SOFALookout、SOFATracer、SOFAMosn、SOFAMesh";
    private final String encoded = URLEncodeUtil.encode(str);

    @Benchmark
    @Fork(1)
    @Test
    public void encodeByURLEncodeUtil() throws Exception {
        String encode = URLEncodeUtil.encode(str);
        //System.out.println(encode);
    }

    @Benchmark
    @Fork(1)
    public String encodeByJavaURLEncoder() throws Exception {
        return URLEncoder.encode(str, "UTF-8");
    }

    @Benchmark
    @Fork(1)
    public String encodeByCommonCodecURLCodec() throws Exception {
        return codecer.encode(str);
    }

    @Benchmark
    @Fork(1)
    public String encodeByGuavaUrlEscapers() throws Exception {
        return urlEscaper.escape(str);
    }

    @Benchmark
    @Fork(1)
    @Test
    public void decodeByURLEncodeUtil() throws Exception {
        String decode = URLEncodeUtil.decode(encoded, StandardCharsets.UTF_8);
        //System.out.println(decode);
    }

    @Benchmark
    @Fork(1)
    public String decodeByJavaURLEncoder() throws Exception {
        return URLDecoder.decode(encoded, "UTF-8");
    }

    @Benchmark
    @Fork(1)
    public String decodeByCommonCodecURLCodec() throws Exception {
        return codecer.decode(encoded);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(URLEncode_Benchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }

    /**
     * Guava 只有 urlEncode 实现，无 urlDecode 实现
     */
    public static class URLEncodeUtil {
        private static final byte ESCAPE_CHAR = '%';
        private static final boolean[] SAFE_CHAR = new boolean[128];
        private static final Supplier<ByteBuffer> supplier = () -> ByteBuffer.allocate(1024);

        private static ByteBuffer current() {
            int nextVarIndex = InternalThreadLocalMap.nextVariableIndex();
            ByteBuffer byteBuffer = AttachThreadUtil.getOrUpdate(nextVarIndex%1024 * 512, supplier);

            byteBuffer.clear();
            return byteBuffer;
        }
        /**
         * UTF-8
         *
         * @param str
         * @return
         */
        public static String encode(String str) {
            if (str == null) {
                return null;
            }

            byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
            ByteBuffer byteBuffer = encode(bytes);

            byteBuffer.flip();

            int length = byteBuffer.remaining();
            bytes = byteBuffer.array();

            return new String(bytes, 0, length, StandardCharsets.ISO_8859_1);
        }

        private static final ByteBuffer encode(final byte[] bytes) {
            if (bytes == null) {
                return null;
            }
            final ByteBuffer byteBuffer = current();
            for (int i = 0; i < bytes.length; i++) {
                byte b = bytes[i];

                if (b > 0 && SAFE_CHAR[b]) {
                    if (b == ' ') {
                        b = '+';
                    }
                    byteBuffer.put(b);
                } else {
                    byteBuffer.put(ESCAPE_CHAR);
                    byteBuffer.putShort(HexUtil.byteToHex(b));
                }
            }
            return byteBuffer;
        }

        public static String decode(String str, Charset charset) {
            if (str == null) {
                return null;
            }
            byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
            ByteBuffer byteBuffer = decode(bytes);

            byteBuffer.flip();

            int length = byteBuffer.remaining();
            bytes = byteBuffer.array();

            return new String(bytes, 0, length, charset);
        }

        private static final ByteBuffer decode(final byte[] bytes) {
            if (bytes == null) {
                return null;
            }

            final ByteBuffer byteBuffer = current();

            try {
                for (int i = 0; i < bytes.length; i++) {
                    final byte b = bytes[i];

                    if (b == '+') {
                        byteBuffer.put((byte) ' ');
                    } else if (b == ESCAPE_CHAR) {

                        short hex = UnsafeUtils.unsafe().getShort(bytes, ARRAY_BYTE_BASE_OFFSET + i + 1);
                        byteBuffer.put(HexUtil.hexToByteLE(hex));

                        i += 2;
                    } else {
                        byteBuffer.put(b);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Invalid URL encoding: ", e);
            }

            return byteBuffer;
        }

    }
}
