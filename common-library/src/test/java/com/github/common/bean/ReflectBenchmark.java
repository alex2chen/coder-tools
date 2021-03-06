package com.github.common.bean;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.github.common.bean.vo.FromBean;
import com.github.common.reflect.ClassUtil;
import com.github.common.reflect.FastMethodInvoker;
import com.github.jvm.util.UnsafeUtils;
import net.sf.cglib.reflect.FastClass;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @Author: alex
 * @Description: JMH参阅: HexBenchmark
 * 性能大比拼:
 * Benchmark                                                Mode  Cnt        Score   Units
 * ReflectBenchmark.directSetField                          thrpt    3  7639201.934  ops/ms
 * ReflectBenchmark.directGetField                          thrpt    3  1617653.987  ops/ms
 * ReflectBenchmark.jdkAgeFieldOffset                       thrpt    3  1559621.524  ops/ms
 * ReflectBenchmark.jdkAgeFieldSet                          thrpt    3   898935.939  ops/ms
 * ReflectBenchmark.asmMethodAccess                         thrpt    3   813278.338  ops/ms
 * ReflectBenchmark.jdkAgeMethodHandle_invokeExact          thrpt    3   725845.311  ops/ms
 * ReflectBenchmark.jdkAgeFieldGet                          thrpt    3   712426.830  ops/ms
 * ReflectBenchmark.jdkAgeMethodHandle_invoke               thrpt    3   596344.086  ops/ms
 * ReflectBenchmark.cgligFastMethod                         thrpt    3   543291.780  ops/ms
 * ReflectBenchmark.fastMethodInvoker                       thrpt    3   493046.947  ops/ms
 * ReflectBenchmark.jdkAgeMethodGet                         thrpt    3   308993.640  ops/ms
 * ReflectBenchmark.jdkAgeMethodHandle_invokeWithArguments  thrpt    3    24285.080  ops/ms
 * @Date: created in 2019/5/6.
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
@Threads(4)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ReflectBenchmark {
    private FromBean fromBean = new FromBean();
    private String methodName = "getDirectAge";

    private Field jdkAgeField = ClassUtil.getAccessibleField(FromBean.class, "age");
    private Method jdkAgeMethod = ClassUtil.getAccessibleMethod(FromBean.class, methodName, long.class);
    private long jdkAgeFieldOffset = UnsafeUtils.unsafe().objectFieldOffset(jdkAgeField);
    private MethodHandle jdkAgeMethodHandle;
    /**
     * 基于cglib
     */
    private FastMethodInvoker cgligFastMethod = FastMethodInvoker.create(FromBean.class, methodName, long.class);
    private MethodAccess asmClz = MethodAccess.get(FromBean.class);
    private int asmMethodIndex = asmClz.getIndex(methodName, long.class);

    public ReflectBenchmark() {
        try {
            //jdkAgeField.setAccessible(true);
            jdkAgeMethodHandle = MethodHandles.publicLookup().findVirtual(FromBean.class, methodName, MethodType.methodType(int.class, long.class));
            //System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "target\\class");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    @Fork(1)
    public int directGetField() throws Exception {
        return fromBean.getAge();
    }

    @Benchmark
    @Fork(1)
    public void directSetField() throws Exception {
        fromBean.setAge(18);
    }

    @Benchmark
    @Fork(1)
    public int jdkAgeFieldGet() throws IllegalAccessException {
        return jdkAgeField.getInt(fromBean);
    }

    @Benchmark
    @Fork(1)
    public void jdkAgeFieldSet() throws IllegalAccessException {
        jdkAgeField.setInt(fromBean, 20);
    }

    @Benchmark
    @Fork(1)
    public int jdkAgeMethodGet() throws InvocationTargetException, IllegalAccessException {
        return (int) jdkAgeMethod.invoke(fromBean, 12L);
    }

    @Benchmark
    @Fork(1)
    public int jdkAgeMethodHandle_invoke() throws Throwable {
        return (int) jdkAgeMethodHandle.invoke(fromBean, Long.valueOf(34L));
    }

    @Benchmark
    @Fork(1)
    public int jdkAgeMethodHandle_invokeExact() throws Throwable {
        return (int) jdkAgeMethodHandle.invokeExact(fromBean, 34L);
    }

    @Benchmark
    @Fork(1)
    public int jdkAgeMethodHandle_invokeWithArguments() throws Throwable {
        return (int) jdkAgeMethodHandle.invokeWithArguments(new Object[]{fromBean, Long.valueOf(34L)});
    }


    @Benchmark
    @Fork(1)
    public Object jdkAgeFieldOffset() {
        return UnsafeUtils.unsafe().getObject(fromBean, jdkAgeFieldOffset);
    }

    @Benchmark
    @Fork(1)
    public int cgligFastMethod() throws InvocationTargetException {
        return (int) cgligFastMethod.invoke(fromBean, new Object[]{23L});
    }

    @Benchmark
    @Fork(1)
    public int asmMethodAccess() {
        return (int) asmClz.invoke(fromBean, asmMethodIndex, 23L);
    }

    @Benchmark
    @Fork(1)
    public int fastMethodInvoker() {
        return (int) cgligFastMethod.invoke(fromBean, 23L);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ReflectBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}