package com.github.common.bean;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.List;

/**
 * @author alex.chen
 * @Description:
 * @date 2017/4/27
 */
public class BeanCopy_test {
    private FromBean fromBean = new FromBean();
    private IMethodCallBack beanUtil;
    private IMethodCallBack propertyUtil;
    private IMethodCallBack springBeanUtil;
    private IMethodCallBack springBeanCopier;
    private IMethodCallBack cglibBeanCopier;
    /**
     * BeanUtil.copyProperties[commons-beanutils] 开始进行测试: 耗时: 78.33 ms
     * PropertyUtils.copyProperties[commons-beanutils] 开始进行测试: 耗时: 7.554 ms
     * BeanUtils.copyProperties[spring-beans] 开始进行测试: 耗时: 43.83 ms
     * BeanCopier.create[spring-beans] 开始进行测试: 耗时: 94.30 μs
     * BeanCopier.create[cglib] 开始进行测试: 耗时: 94.62 μs
     */
    @Test
    public void benchmark() {
        int times = 100;
        runTimes(times, beanUtil, fromBean);
        runTimes(times, propertyUtil, fromBean);
        runTimes(times, springBeanUtil, fromBean);
        runTimes(times, springBeanCopier, fromBean);
        runTimes(times, cglibBeanCopier, fromBean);
    }
    @Before
    public void init() {
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

    private void runTimes(int times, IMethodCallBack m, FromBean frombean) {
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            FromBean tobean = null;
            System.out.print(m.getMethodName() + " 开始进行测试: ");
            for (int i = 0; i < times; i++) {
                tobean = m.callMethod(frombean);
            }
            System.out.print("耗时: " + stopwatch);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    interface IMethodCallBack {
        String getMethodName();

        FromBean callMethod(FromBean frombean) throws Exception;
    }

    public class FromBean implements Serializable, Cloneable {
        private String name;
        private int age;
        private String address;
        private String idno;
        private double money;
        private List<String> alias;

        public FromBean() {
            alias = Lists.newArrayList();
        }

        public FromBean(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getIdno() {
            return idno;
        }

        public void setIdno(String idno) {
            this.idno = idno;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public List<String> getAlias() {
            return alias;
        }

        public void setAlias(List<String> alias) {
            this.alias = alias;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            FromBean o = null;
            try {
                o = (FromBean) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return o;
        }

        @Override
        public String toString() {
            return "FromBean{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", address='" + address + '\'' +
                    ", idno='" + idno + '\'' +
                    ", money=" + money +
                    ", alias=" + alias +
                    '}';
        }
    }
}
