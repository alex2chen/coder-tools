package com.github.common.service;

/**
 * @Author: alex.chen
 * @Description:
 * @Date: 2019/10/24
 */
public interface EchoService {

    String hi();

    String echo(String message);

    String echo(Integer type, String message);

    @Deprecated
    String bye();
}