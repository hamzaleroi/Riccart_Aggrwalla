package com.esi.Multicast;

import com.esi.Entities.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;

public class MulticastPublisher {
    private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;

    public void multicast(
            Message multicastMessage, int port) throws IOException, SocketException {
        buf = new byte[256];



        socket = new DatagramSocket();
        InetAddress group = InetAddress.getLocalHost();



        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(multicastMessage);

        buf = json.getBytes();
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, group, port);


        socket.send(packet);
        socket.close();
    }
}
