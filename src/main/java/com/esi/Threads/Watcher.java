package com.esi.Threads;



public class Watcher extends Node {
    public Watcher(int num, int port) {
        super(num, port);

    }
    @Override
    public void run() {

        this.setListener(new WatcherListener(this));
        this.getListener().start();
    }


}
