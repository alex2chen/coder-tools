package com.github.cache.redis;

import org.junit.Before;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/9/4.
 */
public class Redisson_test {
    private RedissonClient redisson;

    @Before
    public void init() {
        Config config = new Config();
        config.setCodec(new StringCodec());
        config.useSingleServer().setAddress("redis://192.168.2.77:6379");
        config.useSingleServer().setPassword("Kxtx888");//设置密码
        config.useSingleServer().setConnectionPoolSize(500);//设置对于master节点的连接池中连接数最大为500
        config.useSingleServer().setIdleConnectionTimeout(10000);//如果当前连接池里的连接数量超过了最小空闲连接数，而同时有连接空闲时间超过了该数值，那么这些连接将会自动被关闭，并从连接池里去掉。时间单位是毫秒。
        config.useSingleServer().setConnectTimeout(30000);//同任何节点建立连接时的等待超时。时间单位是毫秒。
        config.useSingleServer().setTimeout(3000);//等待节点回复命令的时间。该时间从命令发送成功时开始计时。
        config.useSingleServer().setPingTimeout(30000);
        redisson = Redisson.create(config);
    }

    @Test
    public void lock() {
        String key = "oms-9527";
        RLock mylock = redisson.getLock(key);
        mylock.lock(5, TimeUnit.SECONDS);
        System.out.println("lock");
        mylock.unlock();
    }
}
