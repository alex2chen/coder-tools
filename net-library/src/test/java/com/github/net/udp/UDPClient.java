package com.github.net.udp;

import org.junit.Test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * @Author: alex
 * @Description
 * @Date: created in 2015/7/18.
 */
public class UDPClient {

    @Test
    public void request() {
        try {
            System.out.println("udp client 9999 runing");
            byte[] _reqData = "hello".getBytes();
            DatagramPacket _datapg = new DatagramPacket(_reqData, _reqData.length, new InetSocketAddress("127.0.0.1", 5678));

            DatagramSocket _udpclient = new DatagramSocket(9999);
            _udpclient.send(_datapg);
            _udpclient.close();
            System.out.println("udp client close");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
