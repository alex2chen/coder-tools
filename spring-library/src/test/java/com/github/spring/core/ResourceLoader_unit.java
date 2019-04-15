package com.github.spring.core;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/4/15.
 */
public class ResourceLoader_unit {
    @Test
    public void loadPrjFile() {
        //访问项目路径
        File file = new File("");
        System.out.println(file.getAbsolutePath());//C:\Users\fei.chen\Desktop\demo
        //System中props值实际上是通过 Properties initProperties(Properties props)
        System.out.println(System.getProperty("user.dir"));//C:\Users\fei.chen\Desktop\demo
        //访问项目中的文件资源
        /**
         * 思考1：ClassLoader.getResourceAsStream为何可以读取到本地文件资源？
         * 通过java.exe -classpath 项目的类根路径（C:\Users\fei.chen\Desktop\demo\target\test-classes）
         * 思考2：ResourceLoader_unit.getResource和Thread.currentThread().getContextClassLoader().getResource读取路径有何不同？
         * 通结果就知道了，ResourceLoader_unit，可以识别/（代表项目的类根路径），ClassLoader默认是从项目的类根路径读取，不能识别/
         */
        URL url = ResourceLoader_unit.class.getResource("/com/example/a.txt");
        System.out.println(url);//file:/C:/Users/fei.chen/Desktop/demo/target/test-classes/com/example/a.txt
        System.out.println(ResourceLoader_unit.class.getResource("a.txt"));//file:/C:/Users/fei.chen/Desktop/demo/target/test-classes/com/example/jvm/net/a.txt
        System.out.println(ResourceLoader_unit.class.getClassLoader().getResource("application.properties"));//file:/C:/Users/fei.chen/Desktop/demo/target/test-classes/application.properties
        System.out.println(ResourceLoader_unit.class.getClassLoader().getResource("/com/example/a.txt"));//null
        System.out.println("url.handler.getClass().getName()值为：sun.net.www.protocol.file.Handler");//这是共同点
    }

    @Test
    public void loadJarFile() {
        URL url = ApplicationContext.class.getClassLoader().getResource("META-INF/license.txt");
        System.out.println(url);//jar:file:/D:/Program/repository/org/springframework/spring-web/4.3.9.RELEASE/spring-web-4.3.9.RELEASE.jar!/META-INF/license.txt
    }

    @Test
    public void loadClasspathFile() throws IOException {
        //URL url = new URL("classpath:/META-INF/license.txt");
        //System.out.println(url);//报错：java.net.MalformedURLException: unknown protocol: classpath
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:/META-INF/license.txt");
        System.out.println(resource.getURL());

        //拓展协议
        ((DefaultResourceLoader) resourceLoader).addProtocolResolver(new ProtocolResolver() {
            private static final String PROTOCOL_PREFIX = "cp:/";

            @Override
            public Resource resolve(String location, ResourceLoader resourceLoader) {
                if (location.startsWith(PROTOCOL_PREFIX)) {
                    String classpath = ResourceLoader.CLASSPATH_URL_PREFIX + location.substring(PROTOCOL_PREFIX.length());
                    return resourceLoader.getResource(classpath);
                }
                return null;
            }
        });
        Resource resource1 = resourceLoader.getResource("cp:/application.properties");
        System.out.println(resource1.getURL());
    }
}
