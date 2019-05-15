package com.github.common.io;

import com.google.common.base.Stopwatch;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: alex
 * @Description: JMH参阅: HexBenchmark
 * 利用 IO 零拷贝的 MQ 们
 * sendFile：FileChannel，非零拷贝，基于 block
 * mmap：基于OS的mmap 的内存映射技术，通过 MMU 映射文件，使随机读写文件和读写内存相似的速度
 * 读 4kb 以下的数据请使用 mmap，大于 4kb请使用 FileChannel
 * @Date: created in 2018/5/15.
 */
public class SendFile_mmap_Test {
    private long fileSize = 1024 * 1024 * 1024;
    private static int[] lenArr = {32, 64, 128, 512, 1024, 2048, 4096, 8192, 16384, 134217728, 1073741824};

    @Test
    public void rwfile32() throws IOException {
        int byteSize = 32;
        File file = writeRandomAccessFile(byteSize);
        File file1 = writeFileOutputStream(byteSize);
        File file2 = writeFileChannel(byteSize);
        File file3 = writeMappedByteBuffer(byteSize);
        readRandomAccessFile(file, byteSize);
        readFileChannel(file2, byteSize);
        readMappedByteBuffer(file3, byteSize);
    }
    @Test
    public void writeAsyncForce32() throws IOException {
        int byteSize = 32;
        writeAsyncForceFileChannel(byteSize);
        writeAsyncForceMappedByteBuffer(byteSize);
    }
    @Test
    public void rwfile64() throws IOException {
        int byteSize = 64;
        File file = writeRandomAccessFile(byteSize);
        File file1 = writeFileOutputStream(byteSize);
        File file2 = writeFileChannel(byteSize);
        File file3 = writeMappedByteBuffer(byteSize);
        readRandomAccessFile(file, byteSize);
        readFileChannel(file2, byteSize);
        readMappedByteBuffer(file3, byteSize);
    }
    @Test
    public void writeAsyncForce64() throws IOException {
        int byteSize = 64;
        writeAsyncForceFileChannel(byteSize);
        writeAsyncForceMappedByteBuffer(byteSize);
    }

    @Test
    public void rwfile4096() throws IOException {
        int byteSize = 4096;
        File file = writeRandomAccessFile(byteSize);
        File file1 = writeFileOutputStream(byteSize);
        File file2 = writeFileChannel(byteSize);
        File file3 = writeMappedByteBuffer(byteSize);
        readRandomAccessFile(file, byteSize);
        readFileChannel(file2, byteSize);
        readMappedByteBuffer(file3, byteSize);
    }
    @Test
    public void writeAsyncForce4096() throws IOException {
        int byteSize = 4096;
        writeAsyncForceFileChannel(byteSize);
        writeAsyncForceMappedByteBuffer(byteSize);
    }
    @Test
    public void rwfile8192() throws IOException {
        int byteSize = 8192;
        File file = writeRandomAccessFile(byteSize);
        File file1 = writeFileOutputStream(byteSize);
        File file2 = writeFileChannel(byteSize);
        File file3 = writeMappedByteBuffer(byteSize);
        readRandomAccessFile(file, byteSize);
        readFileChannel(file2, byteSize);
        readMappedByteBuffer(file3, byteSize);
    }
    @Test
    public void writeAsyncForce8192() throws IOException {
        int byteSize = 8192;
        writeAsyncForceFileChannel(byteSize);
        writeAsyncForceMappedByteBuffer(byteSize);
    }
    private File writeRandomAccessFile(int lenth) throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        File file = getFile("RandomAccessFile");
        if (!file.exists()) file.createNewFile();
        RandomAccessFile ra = new RandomAccessFile(file, "r");
        while (true) {
            byte[] arr = new byte[lenth];
            int byteSize = ra.read(arr);
            if (byteSize == -1) {
                break;
            }
        }
        logFormat("writeRandomAccessFile", lenth, stopwatch);
        return file;
    }

    private void readRandomAccessFile(File file, int lenth) throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        RandomAccessFile ra = new RandomAccessFile(file, "r");
        while (true) {
            byte[] arr = new byte[lenth];
            int len = ra.read(arr);
            if (len == -1) {
                break;
            }
        }
        logFormat("readRandomAccessFile", lenth, stopwatch);
    }

    private File writeFileOutputStream(int lenth) throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        File file = getFile("FileOutputStream");
        FileOutputStream fo = new FileOutputStream(file);
        byte[] arr = new byte[lenth];
        Arrays.fill(arr, (byte) 2);
        int length = 0;
        while (length < fileSize) {
            length += arr.length;
            fo.write(arr);
        }
        //fo.flush();
        logFormat("writeFileOutputStream", lenth, stopwatch);
        return file;
    }

    private File writeFileChannel(int lenth) throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        File file = getFile("RandomAccessFile");
        FileChannel fc = new RandomAccessFile(file, "rw").getChannel();
        byte[] arr = new byte[lenth];
        Arrays.fill(arr, (byte) 2);
        int length = 0;
        ByteBuffer b = ByteBuffer.wrap(arr);
        while (length < fileSize) {
            length += arr.length;
            fc.write(b);
        }
        //fc.force(true);
        logFormat("writeFileChannel", lenth, stopwatch);
        return file;
    }

    private void readFileChannel(File file, int lenth) throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        FileChannel fc = new RandomAccessFile(file, "rw").getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(lenth);
        while (true) {
            int len = fc.read(byteBuffer);
            byteBuffer.clear();
            if (len == -1) {
                break;
            }
        }
        logFormat("readFileChannel", lenth, stopwatch);
    }

    private File writeMappedByteBuffer(int lenth) throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        File file = getFile("MappedByteBuffer");
        MappedByteBuffer mb = new RandomAccessFile(file, "rw").getChannel().map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
        byte[] arr = new byte[lenth];
        Arrays.fill(arr, (byte) 2);
        int length = 0;
        while (length < mb.capacity()) {
            length += arr.length;
            mb.put(arr);
        }
        //mb.force();
        logFormat("writeMappedByteBuffer", lenth, stopwatch);
        return file;
    }

    private void readMappedByteBuffer(File file, int lenth) throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        MappedByteBuffer mb = new RandomAccessFile(file, "rw").getChannel().map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
        byte[] arr = new byte[lenth];
        while (mb.hasRemaining()) {
            mb.get(arr);
            arr = new byte[lenth];
        }
        logFormat("readMappedByteBuffer", lenth, stopwatch);
    }

    private static volatile int curLen = 0;
    private int page4 = 1024 * 4 * 4;

    private void writeAsyncForceFileChannel(int lenth) throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        File file = getFile("FileChannel");
        FileChannel fc = new RandomAccessFile(file, "rw").getChannel();
        byte[] arr = new byte[lenth];
        Arrays.fill(arr, (byte) 2);
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    if (curLen < page4 && curLen % page4 == 0) {
                        try {
                            fc.force(false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (latch.getCount() == 0) {
                        break;
                    }
                }
            }
        }).start();
        ByteBuffer b = ByteBuffer.wrap(arr);
        while (curLen < fileSize) {
            curLen += arr.length;
            fc.write(b);
        }
        curLen=0;
        latch.countDown();
        logFormat("writeAsyncForceFileChannel", lenth, stopwatch);
    }

    private static volatile int curLen2 = 0;

    private void writeAsyncForceMappedByteBuffer(int lenth) throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        File file = getFile("MappedByteBuffer");
        MappedByteBuffer mb = new RandomAccessFile(file, "rw").getChannel().map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
        byte[] arr = new byte[lenth];
        Arrays.fill(arr, (byte) 2);
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    if (curLen2 > page4 && curLen2 % page4 == 0) {
                        mb.force();
                    }
                    if (latch.getCount() == 0) {
                        break;
                    }
                }
            }
        }).start();
        while (curLen2 < mb.capacity()) {
            curLen2 += arr.length;
            mb.put(arr);
        }
        curLen2=0;
        latch.countDown();
        logFormat("writeAsyncForceMappedByteBuffer", lenth, stopwatch);
    }

    private void logFormat(String method, int length, Stopwatch stopwatch) {
        System.out.println(String.format("%s: %s.byte ,耗时：%s", method, length, stopwatch));
    }

    private File getFile(String type) {
        String fileName = String.format("file-%s-%s.txt", type, System.currentTimeMillis());
        File file = new File(fileName);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> file.delete()));
        return file;
    }
}
