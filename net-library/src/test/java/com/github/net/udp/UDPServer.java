package com.github.net.udp;

import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @Author: alex
 * @Description
 * @Date: created in 2015/7/18.
 */
public class UDPServer {
    @Test
    public static void server() {
        try {
            System.out.println("udp server 5678 runing");
            byte[] _buf = new byte[1024];
            DatagramPacket _datapg = new DatagramPacket(_buf, _buf.length);
            DatagramSocket _udpServer = new DatagramSocket(5678);
            while (true) {
                _udpServer.receive(_datapg);
                System.out.println(new String(_buf, 0, _buf.length));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
