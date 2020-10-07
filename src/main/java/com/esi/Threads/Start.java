package com.esi.Threads;

import java.io.IOException;


public class Start {

    public static final int num_threads = 11;
    public static Node[] nodes;


    public static void main(String[] args) throws IOException {

        nodes = new Node[num_threads];
        for (int i=0;i<num_threads-1;i++){
            Worker worker = new Worker( i,4446+i);
            worker.setWatcher_port(4446+num_threads-1);
            nodes[i]  = worker;
            nodes[i].start();
        }

        nodes[num_threads-1] = new Watcher( num_threads+1,4446+num_threads-1);
        nodes[num_threads-1].start();



}
}
