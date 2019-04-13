package com.github.db;

import com.github.db.order.OrderMapper;
import com.github.db.order.Stock;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author alex.chen
 * @Description:
 * @date 2019/4/13
 */
public class Mybatis_test {

    @Test
    public void create() {
        SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession();
        session.update("com.github.db.order.OrderMapper.schema");
        session.commit();
    }

    @Test
    public void save() {
        create();
        Stock stock = new Stock(1, "A", 30);
        SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession();
        session.insert("com.github.db.order.StockMapper.insertStock", stock);
        session.commit();
        session.close();
    }

    @Test
    public void selectOne() {
        Stock stock = new Stock(1, "A", 30);
        SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession();
        Stock stock1 = session.selectOne("com.github.db.order.StockMapper.selectStock", stock);
        System.out.println(stock1);
    }

    public static class MyBatisUtil {
        private static SqlSessionFactory sqlSessionFactory;

        static {
            String resource = "mybatis-config.xml";
            InputStream inputStream;
            try {
                inputStream = Resources.getResourceAsStream(resource);
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
                sqlSessionFactory.getConfiguration().addMapper(OrderMapper.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static SqlSessionFactory getSqlSessionFactory() {
            return sqlSessionFactory;
        }
    }
}
