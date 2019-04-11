package com.github.cache.redis;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ResourceUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2017/4/11.
 */
public class JedisPool_test {
    private Jedis jedis;

    @Before
    public void init() {
        JedisShardInfo info = new JedisShardInfo("test-redis.kxtx.cn", 6379);
        info.setPassword("Kxtx888");
        jedis = new Jedis(info);
        System.out.println(jedis.info());
    }

    @After
    public void close() {
        jedis.close();
    }

    @Test
    public void scan() {
        String keyPattern = "GPSPAYCACHE*";
        //count 每次扫描多少条记录，值越大消耗的时间越短，但会影响redis性能。建议设为一千到一万
        int count = 200;
        String cursor = ScanParams.SCAN_POINTER_START;
        List<String> list = Lists.newArrayList();
        ScanParams scanParams = new ScanParams();
        scanParams.count(count);
        scanParams.match(keyPattern);
        do {
            ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
            list.addAll(scanResult.getResult());
            cursor = scanResult.getStringCursor();
        } while (!"0".equals(cursor));
        list.forEach(x -> {
                    long expire = jedis.pttl(x);
                    System.out.println(x + ">>>" + expire);
                }
        );
    }

    @Test
    public void runJedisWithReentrantLock() {
        JedisWithReentrantLock redis = new JedisWithReentrantLock(jedis);
        System.out.println(redis.lock("alex"));
        System.out.println(redis.lock("alex"));
        System.out.println(redis.unlock("alex"));
        System.out.println(redis.unlock("alex"));
    }

    @Test
    public void limitRequest() throws IOException {
        String script = readScript("lock.lua");
        String limit = "5";
        String key = "limit_test";
        List<String> keys = Lists.newArrayList(key);
        List<String> args = Lists.newArrayList(limit);
        Object eval = jedis.eval(script, keys, args);
        System.out.println("eval：" + eval);
    }

    @Test
    public void unlock() {
        String script = readScript("lock.lua");
        String key = "lock_test";
        String value = "ok";
        String SET_IF_NOT_EXIST = "NX";
        String SET_WITH_EXPIRE_TIME = "PX";
        long expireTime = 1000;
        Object result = jedis.set(key, value, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        System.out.println(result);
        Object eval = jedis.eval(script, Lists.newArrayList(key), Lists.newArrayList(value));
        System.out.println("eval：" + eval);
    }

    private String readScript(String script) {
        try {
            //readScript("META-INF/spring.factories")
            URL resource = ResourceUtils.getURL("classpath:" + script);
            if (resource != null) {
                return IOUtils.toString(resource, Charset.defaultCharset());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
