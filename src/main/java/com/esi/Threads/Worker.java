package com.esi.Threads;



import com.esi.Entities.Message;
import com.esi.Entities.MessageType;
import com.esi.Multicast.MulticastPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Worker extends Node {


    // variables de l'algorithme Ricart Agrawalla:
    public int osn=0;
    public int hsn=0;
    public int nbrePAttendus;
    public boolean scDemande;
    public boolean repDiffere[] ;
    public boolean priorite;
    
    private int watcher_port;

    public void setWatcher_port(int watcher_port) {
        this.watcher_port = watcher_port;
    }

    private WorkerListener listener;
    public int token;

    @Override
    public WorkerListener getListener() {
        return listener;
    }

    public void setListener(WorkerListener listener) {
        this.listener = listener;
    }

    public static void critical_section(int index){
        long l = (long) (Math.random() *2000 + 1000);
        System.out.println("thread " + index + "  is in crtical section for " + l + " ms");
        try {
            sleep(l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Done with SC at" + index);
    }
    public  int loop_endlessly () throws InterruptedException, IOException {
        while (true){
            long l = (long) (Math.random() *9000 + 1000);
            System.out.println("thread " + this.index + "  will sleep for " + l + " ms");
            sleep(l);

            // Riccard et Aggrwalla
            System.out.println("Trying to get to the SC");
            this.ricart_aggrwalla_prelude();
            Worker.critical_section(this.getIndex());
            this.ricart_aggrwalla_postlude();

        }

    }

    public void notifyThread(){
        this.notifyAll();
    }
    private synchronized void ricart_aggrwalla_prelude() throws IOException, InterruptedException {
        this.scDemande = true;
        this.osn = hsn;
        this.nbrePAttendus = this.n -1 ;
        multicast(MessageType.Req,this.osn, this.getIndex());
        this.wait();
    }
    private void ricart_aggrwalla_postlude() throws IOException {
        for(int i=0;i<n;i++){
            if (this.repDiffere[i]) {
                this.repDiffere[i] = false;
                Random f = new Random();
                int token = f.nextInt();

                this.sendToIndex(new Message(MessageType.Rep,this.osn,this.getIndex(),i),i);
            }
        }
        this.scDemande = false;

    }


    @Override
    public void run() {
        Random f = new Random();
        this.token =  f.nextInt();

        repDiffere =   new boolean[this.n];
        for(int i=0;i< this.n ; i++){
            this.repDiffere[i]  = false;
        }
        try {
            this.setListener(new WorkerListener(this));
            this.getListener().start();
            this.getListener().token =  this.token;
            loop_endlessly();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void sendToIndex(Message s,int i) throws IOException {
        MulticastPublisher publisher  = new MulticastPublisher();
        publisher.multicast(s,this.getPort() - this.index  + i);

        publisher.multicast(s,watcher_port);
    }

    @Override
    protected void multicast(MessageType type, int timestamp, int sender_index) throws IOException {

        for (int i=0; i< n; i++)
        {
            if(i != sender_index )
            {

                Message msg = new Message(type,
                        timestamp,
                        sender_index,
                        i);

                this.sendToIndex(msg,i);
            }

        }

    }
    public Worker(int num, int port){
        super(num, port);
        System.out.println("Thread having the number "+ num + " created");
    }

}
