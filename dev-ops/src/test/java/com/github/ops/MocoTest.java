package com.github.ops;

import com.github.dreamhead.moco.HttpServer;
import com.github.dreamhead.moco.Moco;
import com.github.dreamhead.moco.MocoJsonRunner;
import com.github.dreamhead.moco.Runner;
import com.github.dreamhead.moco.bootstrap.arg.HttpArgs;
import com.github.dreamhead.moco.bootstrap.arg.StartArgs;
import com.github.dreamhead.moco.runner.RunnerFactory;
import com.github.dreamhead.moco.runner.ShutdownRunner;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/5/26
 */
public class MocoTest {
    private String MONITOR_KEY = "_MOCO_MAVEN_PLUGIN_MONITOR_KEY";
    private String SHUTDOWN_PORT_PROPERTY_NAME = "moco.shutdown.port";
    private Integer port = 12306;
    private Integer shutdownPort = 12307;

    @Before
    public void setup() {

    }

    @Test
    public void go_run() throws IOException {
//        HttpServer server = Moco.httpServer(port);
//        server.response("foo");
        HttpServer server = MocoJsonRunner.jsonHttpServer(port, Moco.file("E:\\code\\mvn-train\\DevOps\\src\\test\\java\\config.json"));
        Runner runner = Runner.runner(server);
        runner.start();
//        Content content = Request.Get("http://localhost:12306").execute().returnContent();
//        System.out.println(content);
        try {
            //必须
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void go_start() throws IOException {
        File configFile = new File("E:\\code\\mvn-train\\DevOps\\src\\test\\java\\config.json");
        if (!configFile.exists()) {
            return;
        }
        StartArgs startArgs = HttpArgs.httpArgs().withPort(port).withShutdownPort(shutdownPort).withConfigurationFile(configFile.getAbsolutePath()).build();
        ShutdownRunner runner = new RunnerFactory(MONITOR_KEY).createRunner(startArgs);
        runner.run();
        System.setProperty(SHUTDOWN_PORT_PROPERTY_NAME, Integer.valueOf(runner.shutdownPort()).toString());
        System.out.println("Start Moco server.");
        Content content = Request.Get("http://localhost:12306/getJson").execute().returnContent();
        System.out.println(content);
    }

    @Test
    public void go_stop() throws IOException {
        Integer actualStopPort = Integer.valueOf(System.getProperty(SHUTDOWN_PORT_PROPERTY_NAME));
        Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), actualStopPort);
        socket.setSoLinger(false, 0);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write((MONITOR_KEY + "\r\n").getBytes());
        outputStream.flush();
        socket.close();
        System.out.println("Stopped Moco server.");
    }
}
