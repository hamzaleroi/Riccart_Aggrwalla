package com.esi.Threads;

import com.esi.Entities.Message;
import com.esi.Entities.MessageType;
import com.esi.Multicast.MulticastPublisher;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.Queue;

public class Node implements Runnable {
    public int n = Start.num_threads -1;
    protected int index;
    protected int port;
    private ServerSocket server = null;
    private DatagramSocket client = null;
    private Thread t;
    private Listener listener;






    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public Node(int num, int port) {
        index = num;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public int getIndex() {
        return index;
    }

    protected void sendToIndex(Message s,int i) throws IOException {
        MulticastPublisher publisher  = new MulticastPublisher();
        publisher.multicast(s,this.getPort() - this.index  + i);

    }
    protected void multicast(MessageType type, int timestamp, int sender_index) throws IOException {

        for (int i=0; i< n; i++)
            {
                if(i != sender_index )
                    {
                        System.out.println("Process  " + this.getIndex() + " is sending to "+ i + " a "+ type + " at " + this.getClass());
                    Message msg = new Message(type,
                            timestamp,
                            sender_index,
                            i);
                    System.out.println(msg);
                    this.sendToIndex(msg,i);
                    }

            }

    }





    public void start(){
        if (t == null){
            t = new Thread(this,"Thread"+this.index);
            t.start();
        }
    }

    @Override
    public void run() {

    }
}
