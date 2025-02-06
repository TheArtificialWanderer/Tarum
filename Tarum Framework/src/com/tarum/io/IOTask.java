package com.tarum.io;

public abstract class IOTask {

    private long uid;
    private int id;
    private String name;
    private State state = State.PENDING;
    private Priority priority = Priority.NORMAL;

    private long creationTime, enqueuedTime, startTime, pauseTime, stopTime, completionTime;

    public enum State{
        PENDING, INITIALIZATION, STARTED, PAUSED, STOPPED, COMPLETED
    }
    public enum Priority{
        LOW, NORMAL, HIGH
    }

    public IOTask(){
    }



}
