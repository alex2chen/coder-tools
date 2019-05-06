package com.github.common.bean;

import com.github.common.bean.vo.FromBean;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author alex.chen
 * @Description: JMH参阅: HexBenchmark
 * 性能大比拼:
 * Benchmark                             Mode  Cnt       Score   Units
 * BeanCopy_Benchmark.directCopy        thrpt    3  359135.157  ops/ms
 * BeanCopy_Benchmark.springBeanCopier  thrpt    3  357654.895  ops/ms
 * BeanCopy_Benchmark.cglibBeanCopier   thrpt    3  352030.125  ops/ms
 * BeanCopy_Benchmark.springBeanUtil    thrpt    3   14870.658  ops/ms
 * BeanCopy_Benchmark.propertyUtil      thrpt    3    1172.622  ops/ms
 * BeanCopy_Benchmark.beanUtil          thrpt    3     579.760  ops/ms
 * @date 2017/4/27
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
@Threads(4)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BeanCopy_Benchmark {
    private FromBean fromBean = new FromBean();
    private IMethodCallBack beanUtil;
    private IMethodCallBack propertyUtil;
    private IMethodCallBack springBeanUtil;
    private IMethodCallBack springBeanCopier;
    private IMethodCallBack cglibBeanCopier;

    public BeanCopy_Benchmark() {
        fromBean.setAddress("北京市朝阳区大屯路");
        fromBean.setAge(20);
        fromBean.setMoney(30000.111);
        fromBean.setIdno("110330219879208733");
        fromBean.setName("test");
        fromBean.setAlias(Lists.newArrayList("a", "1", "z"));
        beanUtil = new IMethodCallBack() {
            @Override
            public String getMethodName() {
                return "BeanUtil.copyProperties[commons-beanutils]";
            }

            @Override
            public FromBean callMethod(FromBean frombean) throws Exception {
                FromBean toBean = new FromBean();
                org.apache.commons.beanutils.BeanUtils.copyProperties(toBean, frombean);
                return toBean;
            }
        };
        propertyUtil = new IMethodCallBack() {
            @Override
            public String getMethodName() {
                return "PropertyUtils.copyProperties[commons-beanutils]";
            }

            @Override
            public FromBean callMethod(FromBean frombean) throws Exception {
                FromBean toBean = new FromBean();
                org.apache.commons.beanutils.PropertyUtils.copyProperties(toBean, frombean);
                return toBean;
            }
        };
        springBeanUtil = new IMethodCallBack() {
            @Override
            public String getMethodName() {
                return "BeanUtils.copyProperties[spring-beans]";
            }

            @Override
            public FromBean callMethod(FromBean frombean) throws Exception {
                FromBean toBean = new FromBean();
                org.springframework.beans.BeanUtils.copyProperties(frombean, toBean);
                return toBean;
            }
        };
        springBeanCopier = new IMethodCallBack() {
            org.springframework.cglib.beans.BeanCopier bc = org.springframework.cglib.beans.BeanCopier.create(FromBean.class, FromBean.class, false);

            @Override
            public String getMethodName() {
                return "BeanCopier.create[spring-beans]";
            }

            @Override
            public FromBean callMethod(FromBean frombean) throws Exception {
                FromBean toBean = new FromBean();
                bc.copy(frombean, toBean, null);
                return toBean;
            }
        };
        cglibBeanCopier = new IMethodCallBack() {
            net.sf.cglib.beans.BeanCopier bc = net.sf.cglib.beans.BeanCopier.create(FromBean.class, FromBean.class, false);

            @Override
            public String getMethodName() {
                return "BeanCopier.create[cglib]";
            }

            @Override
            public FromBean callMethod(FromBean frombean) throws Exception {
                FromBean toBean = new FromBean();
                bc.copy(frombean, toBean, null);
                return toBean;
            }
        };

    }

    @Benchmark
    @Fork(1)
    public void directCopy() {
        FromBean toBean = new FromBean();
        toBean.setAge(fromBean.getAge());
        toBean.setAddress(fromBean.getAddress());
        toBean.setAlias(fromBean.getAlias());
        toBean.setIdno(fromBean.getIdno());
        toBean.setMoney(fromBean.getMoney());
        toBean.setName(fromBean.getName());
    }

    @Benchmark
    @Fork(1)
    public void beanUtil() throws Exception {
        beanUtil.callMethod(fromBean);
    }

    @Benchmark
    @Fork(1)
    public void propertyUtil() throws Exception {
        propertyUtil.callMethod(fromBean);
    }

    @Benchmark
    @Fork(1)
    public void springBeanUtil() throws Exception {
        springBeanUtil.callMethod(fromBean);
    }

    @Benchmark
    @Fork(1)
    public void springBeanCopier() throws Exception {
        springBeanCopier.callMethod(fromBean);
    }

    @Benchmark
    @Fork(1)
    public void cglibBeanCopier() throws Exception {
        cglibBeanCopier.callMethod(fromBean);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BeanCopy_Benchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }

    interface IMethodCallBack {
        String getMethodName();

        FromBean callMethod(FromBean frombean) throws Exception;
    }
}
