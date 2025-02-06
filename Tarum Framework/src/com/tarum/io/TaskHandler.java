package com.tarum.io;

public class TaskHandler implements Runnable{

    private IOTask currentTask;

    private long lastTaskProcessOperationPerformedTime;

    @Override
    public void run() {

    }

    private void process (){
        long delta = System.currentTimeMillis();



        this.lastTaskProcessOperationPerformedTime = System.currentTimeMillis();
    }

}
