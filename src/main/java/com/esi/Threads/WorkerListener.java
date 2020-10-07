package com.esi.Threads;

import com.esi.Entities.Message;
import com.esi.Entities.MessageType;

import java.io.IOException;
import java.util.Random;

/**
 * Created by swair on 6/17/14.
 */
public class WorkerListener extends Listener {

    public int token;

    public WorkerListener(Worker n) {
        //this.getNode_reference().server = server;
        super(n);


    }

    @Override
    public Worker getNode_reference() {
        return (Worker) super.getNode_reference();
    }

    @Override
    public synchronized Message reflect_on_receive(String s) throws IOException {


        Message msg = super.reflect_on_receive(s);
        switch (msg.type) {
            case Req:

                this.getNode_reference().hsn  = Math.max(this.getNode_reference().hsn,msg.timestamp) + 1;
                this.getNode_reference().priorite = this.getNode_reference().scDemande && ((msg.timestamp > this.getNode_reference().osn) || ((this.getNode_reference().osn == msg.timestamp) && (this.getNode_reference().getIndex() < msg.sender_index)));


                if (this.getNode_reference().priorite) {
                    this.getNode_reference().repDiffere[msg.sender_index] = true;
                }
                else{
                    Random f = new Random();
                    int token = f.nextInt();

                    this.getNode_reference().sendToIndex(new Message(MessageType.Rep,this.getNode_reference().osn,this.getNode_reference().getIndex(),msg.sender_index),msg.sender_index);
                }
                break;
            case Rep:
                this.getNode_reference().nbrePAttendus --;
                if (this.getNode_reference().nbrePAttendus == 0) {

                    synchronized(this.getNode_reference()) {
                        try {

                            this.getNode_reference().notifyThread();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
                break;

        }


        return msg;
    }



}