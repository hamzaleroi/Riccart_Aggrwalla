package com.esi.Threads;

import com.esi.Entities.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Listener extends  Thread{
    //private ServerSocket server;
    private Node node_reference;

    public Node getNode_reference() {
        return node_reference;
    }

    public Listener(Node node_reference) {
        this.node_reference = node_reference;
    }

    public void run() {
        byte[] buf = new byte[256];

        try {

            DatagramSocket socket = new DatagramSocket(this.node_reference.getPort());

            while(true) {
                try {

                    //socket.joinGroup(group);


                    while (true) {
                        DatagramPacket packet = new DatagramPacket(buf, buf.length);
                        socket.receive(packet);
                        String received = new String(
                                packet.getData(), 0, packet.getLength());

                        ;
                        this.reflect_on_receive(received);
                    }


                } catch(IOException ex) {
                    System.err.println(ex);
                }
            }

        } catch(IOException ex) {
            ex.printStackTrace();
            System.err.println("couldn't start the server");
        }
    }
    public Message reflect_on_receive(String s) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Message msg =  objectMapper.readValue(s, Message.class);
        return msg;

    }
}
