package com.github.common.metrics.support;

import com.codahale.metrics.*;
import com.github.common.metrics.Exporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: alex.chen
 * @Description:
 * @Date: 2019/10/25
 */
public class JmxExporter implements Exporter, MetricRegistryListener {
    private static Logger logger = LoggerFactory.getLogger(JmxExporter.class);
    private MBeanServer mBeanServer;
    private MetricRegistry registry;
    private String domain;
    private final Map<ObjectName, ObjectName> registered;

    public JmxExporter(String domain, MetricRegistry registry) {
        this.mBeanServer = ManagementFactory.getPlatformMBeanServer();
        this.registry = registry;
        this.domain = domain;
        this.registered = new ConcurrentHashMap<ObjectName, ObjectName>();

        registry.addListener(this);
    }

    public void initMBeans() {
        Map<String, Gauge> gauges = registry.getGauges();
        for (Map.Entry<String, Gauge> entry : gauges.entrySet()) {
            onGaugeAdded(entry.getKey(), entry.getValue());
        }
        Map<String, Counter> counters = registry.getCounters();
        for (Map.Entry<String, Counter> entry : counters.entrySet()) {
            onCounterAdded(entry.getKey(), entry.getValue());
        }
        Map<String, Histogram> histograms = registry.getHistograms();
        for (Map.Entry<String, Histogram> entry : histograms.entrySet()) {
            onHistogramAdded(entry.getKey(), entry.getValue());
        }
        Map<String, Timer> timers = registry.getTimers();
        for (Map.Entry<String, Timer> entry : timers.entrySet()) {
            onTimerAdded(entry.getKey(), entry.getValue());
        }
    }

    public void destroyMBeans() {
        Map<String, Gauge> gauges = registry.getGauges();
        for (String key : gauges.keySet()) {
            onGaugeRemoved(key);
        }

        Map<String, Counter> counters = registry.getCounters();
        for (String key : counters.keySet()) {
            onCounterRemoved(key);
        }

        Map<String, Histogram> histograms = registry.getHistograms();
        for (String key : histograms.keySet()) {
            onHistogramRemoved(key);
        }

        Map<String, Timer> timers = registry.getTimers();
        for (String key : timers.keySet()) {
            onTimerRemoved(key);
        }
    }

    private void registerMBean(ObjectName objectName, Object mBean) {
        try {
            ObjectInstance objectInstance = mBeanServer.registerMBean(mBean, objectName);
            if (objectInstance != null) {
                // the websphere mbeanserver rewrites the objectname to include cell, node & server info
                // make sure we capture the new objectName for unregistration
                registered.put(objectName, objectInstance.getObjectName());
            } else {
                registered.put(objectName, objectName);
            }
        } catch (InstanceAlreadyExistsException e) {
            logger.debug("Unable to register already exist mbean:" + objectName, e);
        } catch (JMException e) {
            logger.warn("Unable to register:" + objectName, e);
        }
    }

    private void unregisterMBean(ObjectName originalObjectName) {
        ObjectName storedObjectName = registered.remove(originalObjectName);
        try {
            if (storedObjectName != null) {
                mBeanServer.unregisterMBean(storedObjectName);

            } else {
                mBeanServer.unregisterMBean(originalObjectName);
            }
        } catch (InstanceNotFoundException e) {
            logger.debug("Unable to unregister:" + originalObjectName, e);
        } catch (MBeanRegistrationException e) {
            logger.warn("Unable to unregister:" + originalObjectName, e);
        }
    }

    private ObjectName createObjectName(String name) {
        try {
            return new ObjectName(this.domain, "name", name);
        } catch (MalformedObjectNameException e) {
            try {
                return new ObjectName(this.domain, "name", ObjectName.quote(name));
            } catch (MalformedObjectNameException e1) {
                logger.warn("Unable to register {}", name, e1);
                throw new RuntimeException(e1);
            }
        }
    }

    @Override
    public void onGaugeAdded(String name, Gauge gauge) {
//        ObjectName objectName = createObjectName(name);
//        registerMBean(objectName, new JmxGauge(gauge, objectName));
    }

    @Override
    public void onCounterAdded(String name, Counter counter) {
        ObjectName objectName = createObjectName(name);
        registerMBean(objectName, new JmxCounter(counter, objectName));
    }

    @Override
    public void onHistogramAdded(String name, Histogram histogram) {
        ObjectName objectName = createObjectName(name);
        registerMBean(objectName, new JmxHistogram(histogram, objectName));
    }

    @Override
    public void onTimerAdded(String name, Timer timer) {
        ObjectName objectName = createObjectName(name);
        registerMBean(objectName, new JmxTimer(timer, objectName));
    }

    @Override
    public void onGaugeRemoved(String name) {
        ObjectName objectName = createObjectName(name);
        unregisterMBean(objectName);
    }

    @Override
    public void onCounterRemoved(String name) {
        ObjectName objectName = createObjectName(name);
        unregisterMBean(objectName);
    }

    @Override
    public void onHistogramRemoved(String name) {
        ObjectName objectName = createObjectName(name);
        unregisterMBean(objectName);
    }

    @Override
    public void onMeterAdded(String name, Meter meter) {

    }

    @Override
    public void onMeterRemoved(String name) {

    }

    @Override
    public void onTimerRemoved(String name) {
        ObjectName objectName = createObjectName(name);
        unregisterMBean(objectName);
    }

    ///////// MBean定义///////
    public interface MetricMBean {
        ObjectName objectName();
    }

    public interface JmxCounterMBean extends MetricMBean {
        long getCount();
    }

    public interface JmxHistogramMBean extends MetricMBean {

        long getMin();

        long getMax();

        double get75thPercentile();
    }

    public interface JmxTimerMBean extends MetricMBean {
        long getMax();

        long getMin();
    }

    private abstract static class AbstractBean implements MetricMBean {
        private final ObjectName objectName;

        AbstractBean(ObjectName objectName) {
            this.objectName = objectName;
        }

        @Override
        public ObjectName objectName() {
            return objectName;
        }
    }

    /**
     * Counter类型的JmxMbean, JMX属性名见getter函数名.
     */
    private static class JmxCounter extends AbstractBean implements JmxCounterMBean {
        private final Counter metric;

        private JmxCounter(Counter metric, ObjectName objectName) {
            super(objectName);
            this.metric = metric;
        }

        @Override
        public long getCount() {
            return metric.getCount();
        }
    }

    /**
     * Histogram类型的JmxMbean, JMX属性名见getter函数名.
     */
    private static class JmxHistogram extends AbstractBean implements JmxHistogramMBean {
        private final Histogram metric;

        private JmxHistogram(Histogram metric, ObjectName objectName) {
            super(objectName);
            this.metric = metric;
        }

        @Override
        public long getMin() {
            return metric.getSnapshot().getMin();
        }

        @Override
        public long getMax() {
            return metric.getSnapshot().getMax();
        }

        @Override
        public double get75thPercentile() {
            return metric.getSnapshot().get75thPercentile();
        }

    }

    /**
     * Timer类型的JmxMbean, JMX属性名见getter函数名.
     */
    private static class JmxTimer extends AbstractBean implements JmxTimerMBean {
        private final Timer metric;

        private JmxTimer(Timer metric, ObjectName objectName) {
            super(objectName);
            this.metric = metric;
        }

        @Override
        public long getMax() {
            return metric.getSnapshot().getMax();
        }

        @Override
        public long getMin() {
            return metric.getSnapshot().getMin();
        }

    }
}
