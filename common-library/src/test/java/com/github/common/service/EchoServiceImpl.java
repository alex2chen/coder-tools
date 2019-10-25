package com.github.common.service;

/**
 * @Author: alex.chen
 * @Description:
 * @Date: 2019/10/24
 */
public class EchoServiceImpl implements EchoService {
    @Override
    public String hi() {
        return "hi~";
    }

    @Override
    public String echo(String message) {
        return message;
    }

    @Override
    public String echo(Integer type, String message) {
        return message;
    }

    @Override
    public String bye() {
        return "bye!";
    }
}
