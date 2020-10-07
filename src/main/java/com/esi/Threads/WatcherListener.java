package com.esi.Threads;

import com.esi.App;
import com.esi.Entities.Message;

import java.io.IOException;

public class WatcherListener extends Listener {

    public WatcherListener(Node node_reference) {
        super(node_reference);
    }

    @Override
    public Message reflect_on_receive(String s) throws IOException {

        //System.out.println(s);
        Message msg = super.reflect_on_receive(s);
        try {
            App.add_Message(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return msg;
    }
}
