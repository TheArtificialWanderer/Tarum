package com.tarum.io.task;

public class IOTask {

    private TaskHandler taskHandler;
    private long uid;
    private int id;

    private long creationTime, initializationTime, startTime, pauseTime, stopTime, completionTime;
    private long elapsedTime;

    public IOTask(TaskHandler taskHandler){
        this.taskHandler = taskHandler;
    }

    public void process (long delta){
    }

    public void onStart(){
    }
    public void onInitializationStarted(){
    }
    public void onPause(){
    }
    public void onStop(){
    }
    public void onCompletion(){
    }

}
