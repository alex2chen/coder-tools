package com.github.cache;


import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;
import org.junit.Test;
import org.ehcache.Cache;
import org.ehcache.CacheManager;

import java.net.URI;
import java.net.URL;

import static org.ehcache.clustered.client.config.builders.ClusteredResourcePoolBuilder.clusteredDedicated;
import static org.ehcache.clustered.client.config.builders.ClusteringServiceConfigurationBuilder.cluster;
import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;
import static org.ehcache.config.builders.CacheManagerBuilder.newCacheManager;
import static org.ehcache.config.builders.CacheManagerBuilder.newCacheManagerBuilder;
import static org.ehcache.config.builders.ResourcePoolsBuilder.heap;
import static org.ehcache.config.units.MemoryUnit.MB;
import static org.redisson.misc.URIBuilder.create;

/**
 * @author alex.chen
 * @Description:
 * @date 2017/4/13
 */
public class Ehcache_test {
    @Test
    public void runWithHardCode() {
        try (CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("basicCache", newCacheConfigurationBuilder(Long.class, String.class, heap(100).offheap(1, MB)))
                .build(true)) {
            Cache<Long, String> basicCache = cacheManager.getCache("basicCache", Long.class, String.class);
            basicCache.put(1L, "王爵nice");
            String value = basicCache.get(1L);
        }
    }

    @Test
    public void runWithConfig() {
        Configuration xmlConfig = new XmlConfiguration(Ehcache_test.class.getResource("/ehcache.xml"));
        try (CacheManager cacheManager = newCacheManager(xmlConfig)) {
            cacheManager.init();
            Cache<Long, String> basicCache = cacheManager.getCache("basicCache", Long.class, String.class);
            basicCache.put(1L, "王爵nice");
            String value = basicCache.get(1L);
        }
    }

    @Test
    public void startClusterWithHardCode() {
        final URI uri = create("terracotta://localhost:9510/clustered");
        try (CacheManager cacheManager = newCacheManagerBuilder()
                .with(cluster(uri).autoCreate().defaultServerResource("default-resource"))
                .withCache("basicCache", newCacheConfigurationBuilder(Long.class, String.class,
                        heap(100).offheap(1, MB).with(clusteredDedicated(5, MB))))
                .build(true)) {
            Cache<Long, String> basicCache = cacheManager.getCache("basicCache", Long.class, String.class);
            basicCache.put(1L, "王爵nice");
        }
    }

    @Test
    public void startWithConfig() {
        final URL myUrl = Ehcache_test.class.getResource("/clustered-ehcache.xml");
        Configuration xmlConfig = new XmlConfiguration(myUrl);
        try (CacheManager cacheManager = newCacheManager(xmlConfig)) {
            cacheManager.init();
            Cache<Long, String> basicCache = cacheManager.getCache("basicCache", Long.class, String.class);
            String value = basicCache.get(1L);
        }
    }
}
