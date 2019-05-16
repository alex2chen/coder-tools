package com.github.common.io;

import com.google.common.base.Stopwatch;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

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
 * 读 4kb 以下的数据请使用 mmap，大于 4kb 以上请使用 FileChannel，该理论也看情况：要看读大于写还是反之
 * 简单测试：分值计算规则(32 byte的cost+64 byte的cost)/2
 * write(<4kb):  RandomAccessFile(4.1s)>FileChannel(3.9s)>FileOutputStream(3.9s)>MappedByteBuffer(67ms)
 * read(<4kb):   RandomAccessFile(2.6s)>FileChannel(2.6s)>MappedByteBuffer(69ms)
 * <p>
 * write(>=4kb): FileChannel(150ms)>RandomAccessFile(83.5ms)>FileOutputStream(83ms)>MappedByteBuffer(50ms)
 * read(>=4kb):  RandomAccessFile(197ms)>FileChannel(74ms)>MappedByteBuffer(72ms)
 * <p>
 * 从测试结论上看，4kb时使用FileChannel好像不太明显啊，这是由于windows系统是不支持sendfile的（可参阅FileChannelImpl.transferTo0），所以windows下实际走的是mmap
 * <p>
 * writeRandomAccessFile, 32.byte ,cost：5.420 s
 * writeFileOutputStream, 32.byte ,cost：5.269 s
 * writeFileChannel, 	   32.byte ,cost：5.284 s
 * writeMappedByteBuffer, 32.byte ,cost：81.97 ms
 * readRandomAccessFile,  32.byte ,cost：3.542 s
 * readFileChannel,       32.byte ,cost：3.523 s
 * readMappedByteBuffer,  32.byte ,cost：85.27 ms
 * <p>
 * writeRandomAccessFile, 64.byte ,cost：2.856 s
 * writeFileOutputStream, 64.byte ,cost：2.691 s
 * writeFileChannel,      64.byte ,cost：2.700 s
 * writeMappedByteBuffer, 64.byte ,cost：52.61 ms
 * readRandomAccessFile,  64.byte ,cost：1.807 s
 * readFileChannel,       64.byte ,cost：1.769 s
 * readMappedByteBuffer,  64.byte ,cost：51.21 ms
 * <p>
 * writeRandomAccessFile, 4096.byte ,cost：102.0 ms
 * writeFileOutputStream, 4096.byte ,cost：96.19 ms
 * writeFileChannel,      4096.byte ,cost：149.1 ms
 * writeMappedByteBuffer, 4096.byte ,cost：54.84 ms
 * readRandomAccessFile,  4096.byte ,cost：182.0 ms
 * readFileChannel, 	   4096.byte ,cost：82.41 ms
 * readMappedByteBuffer,  4096.byte ,cost：52.41 ms
 * <p>
 * writeRandomAccessFile, 8192.byte ,cost：65.04 ms
 * writeFileOutputStream, 8192.byte ,cost：70.69 ms
 * writeFileChannel, 	   8192.byte ,cost：152.7 ms
 * writeMappedByteBuffer, 8192.byte ,cost：46.65 ms
 * readRandomAccessFile,  8192.byte ,cost：213.4 ms
 * readFileChannel, 	   8192.byte ,cost：67.68 ms
 * readMappedByteBuffer,  8192.byte ,cost：92.38 ms
 * @Date: created in 2018/5/15.
 */
public class SendFile_mmap_Test {
    //50mb
    private long fileSize = 1024 * 1024 * 100;
    private File randomAccessFile;
    private File outputStreamFile;
    private File fileChannel;
    private File mappedByteBufferFile;

    @Before
    public void init() throws IOException {
        System.out.println("写文件大小为" + fileSize / 1024 / 1024 + "mb，每次写入32byte,64byte,4KB,8KB进行测试");
        System.gc();
        randomAccessFile = getFile("RandomAccessFile", true);
        outputStreamFile = getFile("FileOutputStream", true);
        fileChannel = getFile("FileChannel", true);
        mappedByteBufferFile = getFile("MappedByteBuffer", true);
    }

    @Test
    public void rwfile32() throws IOException {
        int byteSize = 32;
        writeRandomAccessFile(byteSize);
        writeFileOutputStream(byteSize);
        writeFileChannel(byteSize);
        writeMappedByteBuffer(byteSize);
        readRandomAccessFile(byteSize);
        readFileChannel(byteSize);
        readMappedByteBuffer(byteSize);
    }

    //force 对性能影响很大，应该单独测试
    @Test
    public void writeAsyncForce32() throws IOException {
        int byteSize = 32;
        writeAsyncForceFileChannel(byteSize);
        writeAsyncForceMappedByteBuffer(byteSize);
    }

    @Test
    public void rwfile64() throws IOException {
        int byteSize = 64;
        writeRandomAccessFile(byteSize);
        writeFileOutputStream(byteSize);
        writeFileChannel(byteSize);
        writeMappedByteBuffer(byteSize);
        readRandomAccessFile(byteSize);
        readFileChannel(byteSize);
        readMappedByteBuffer(byteSize);
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
        writeRandomAccessFile(byteSize);
        writeFileOutputStream(byteSize);
        writeFileChannel(byteSize);
        writeMappedByteBuffer(byteSize);
        readRandomAccessFile(byteSize);
        readFileChannel(byteSize);
        readMappedByteBuffer(byteSize);
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
        writeRandomAccessFile(byteSize);
        writeFileOutputStream(byteSize);
        writeFileChannel(byteSize);
        writeMappedByteBuffer(byteSize);
        readRandomAccessFile(byteSize);
        readFileChannel(byteSize);
        readMappedByteBuffer(byteSize);
    }

    @Test
    public void writeAsyncForce8192() throws IOException {
        int byteSize = 8192;
        writeAsyncForceFileChannel(byteSize);
        writeAsyncForceMappedByteBuffer(byteSize);
    }

    private void writeRandomAccessFile(int lenth) throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        RandomAccessFile ra = new RandomAccessFile(randomAccessFile, "rw");
        byte[] arr = new byte[lenth];
        Arrays.fill(arr, (byte) 2);
        int count = 0;
        while (count < fileSize) {
            count += arr.length;
            ra.write(arr);
        }
        logFormat("writeRandomAccessFile", lenth, stopwatch);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> IOUtils.closeQuietly(ra)));
    }

    private void readRandomAccessFile(int lenth) throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        RandomAccessFile ra = new RandomAccessFile(randomAccessFile, "r");
        while (true) {
            byte[] arr = new byte[lenth];
            int len = ra.read(arr);
            if (len == -1) {
                break;
            }
        }
        logFormat("readRandomAccessFile", lenth, stopwatch);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> IOUtils.closeQuietly(ra)));
    }

    private void writeFileOutputStream(int lenth) throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        FileOutputStream fo = new FileOutputStream(outputStreamFile);
        byte[] arr = new byte[lenth];
        Arrays.fill(arr, (byte) 2);
        int count = 0;
        while (count < fileSize) {
            count += arr.length;
            fo.write(arr);
        }
        //fo.flush();
        logFormat("writeFileOutputStream", lenth, stopwatch);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> IOUtils.closeQuietly(fo)));
    }

    private void writeFileChannel(int lenth) throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        FileChannel fc = new RandomAccessFile(fileChannel, "rw").getChannel();
        byte[] arr = new byte[lenth];
        Arrays.fill(arr, (byte) 2);
        int count = 0;
        ByteBuffer buf = ByteBuffer.wrap(arr);
        while (count < fileSize) {
            count += arr.length;
            buf.clear();
            buf.put(arr);
            buf.flip();
            while (buf.hasRemaining()) {
                fc.write(buf);
            }
        }
        logFormat("writeFileChannel", lenth, stopwatch);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                fc.force(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    private void readFileChannel(int lenth) throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        FileChannel fc = new RandomAccessFile(fileChannel, "rw").getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(lenth);
        while (true) {
            int len = fc.read(byteBuffer);
            byteBuffer.clear();
            if (len == -1) {
                break;
            }
        }
        logFormat("readFileChannel", lenth, stopwatch);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                fc.force(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    private void writeMappedByteBuffer(int lenth) throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        MappedByteBuffer mb = new RandomAccessFile(mappedByteBufferFile, "rw").getChannel().map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
        byte[] arr = new byte[lenth];
        Arrays.fill(arr, (byte) 2);
        int count = 0;
        while (count < mb.capacity()) {
            count += arr.length;
            mb.put(arr);
        }
        logFormat("writeMappedByteBuffer", lenth, stopwatch);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> mb.force()));
    }

    private void readMappedByteBuffer(int lenth) throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        MappedByteBuffer mb = new RandomAccessFile(mappedByteBufferFile, "rw").getChannel().map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
        byte[] arr = new byte[lenth];
        while (mb.hasRemaining()) {
            mb.get(arr);
            arr = new byte[lenth];
        }
        logFormat("readMappedByteBuffer", lenth, stopwatch);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> mb.force()));
    }

    private static volatile int curLen = 0;
    private int page4 = 1024 * 4 * 4;

    private void writeAsyncForceFileChannel(int lenth) throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        FileChannel fc = new RandomAccessFile(fileChannel, "rw").getChannel();
        byte[] arr = new byte[lenth];
        Arrays.fill(arr, (byte) 2);
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
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
        }).start();
        ByteBuffer buf = ByteBuffer.wrap(arr);
        while (curLen < fileSize) {
            curLen += arr.length;
            buf.clear();
            buf.put(arr);
            buf.flip();
            while (buf.hasRemaining()) {
                fc.write(buf);
            }
        }
        curLen = 0;
        latch.countDown();
        logFormat("writeAsyncForceFileChannel", lenth, stopwatch);
    }

    private static volatile int curLen2 = 0;

    private void writeAsyncForceMappedByteBuffer(int lenth) throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        MappedByteBuffer mb = new RandomAccessFile(mappedByteBufferFile, "rw").getChannel().map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
        byte[] arr = new byte[lenth];
        Arrays.fill(arr, (byte) 2);
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            for (; ; ) {
                if (curLen2 > page4 && curLen2 % page4 == 0) {
                    mb.force();
                }
                if (latch.getCount() == 0) {
                    break;
                }
            }
        }).start();
        while (curLen2 < mb.capacity()) {
            curLen2 += arr.length;
            mb.put(arr);
        }
        curLen2 = 0;
        latch.countDown();
        logFormat("writeAsyncForceMappedByteBuffer", lenth, stopwatch);
    }

    private void logFormat(String method, int length, Stopwatch stopwatch) {
        System.out.println(String.format("%s, %s.byte ,cost：%s", method, length, stopwatch));
    }

    private File getFile(String type, boolean isCreate) throws IOException {
        String fileName = String.format("target/%s-%s.txt", type, System.currentTimeMillis());
        File file = new File(fileName);
        if (!file.exists()) file.createNewFile();
        return file;
    }
}
