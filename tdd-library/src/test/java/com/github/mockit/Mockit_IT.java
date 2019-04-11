package com.github.mockit;

import mockit.NonStrictExpectations;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/11/11
 */
@RunWith(JMockit.class)
public class Mockit_IT {
    @Test
    public void split_test() {
        new NonStrictExpectations(UserACL.class) {
            {
                UserACL.getUserName();//也可以使用参数匹配：ClassMocked.getDouble(anyDouble);
                result = "aaa";
            }
        };
        assertEquals("aaa", UserACL.getUserName());
        new Verifications() {//验证预期结果和行为被调用
            {
                UserACL.getUserName();
                times = 1;
            }
        };
    }
    public static class UserACL {
        /**
         * @return
         */
        public static String getUserName() {
            return "alex";
        }
    }
}
