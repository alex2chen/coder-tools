package com.github.art.创建型模式.工厂方法;

/**
 * FileLogFactory
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/31
 */
public class FileLogFactory extends LogFactory {
    public FileLogFactory() {
    }

    @Override
    public Log createLog() {
        return new FileLog();
    }
}
