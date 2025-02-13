package com.tarum.io.parser;
import com.tarum.io.Configuration;
import com.tarum.io.content.type.BasicContentContainer;
import com.tarum.io.content.ContentManager;

import java.io.File;

public abstract class ContentParser implements Runnable{

    private ContentManager contentManager;
    private Configuration configuration;
    private File file;
    private BasicContentContainer container;

    private State state = State.INITIALIZATION;

    private long initializationTime, startTime, pauseTime, stopTime,
            dispositionStartTime,dispositionCompletionTime;

    public enum State{
        INITIALIZATION, STARTED, PAUSED, STOPPED, DISPOSITION
    }

    public ContentManager getContentManager() {
        return contentManager;
    }
    public void setContentManager(ContentManager contentManager) {
        this.contentManager = contentManager;
    }
    public Configuration getConfig() {
        return configuration;
    }
    public void setConfig(Configuration configuration) {
        this.configuration = configuration;
    }
    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }
    public BasicContentContainer getContainer() {
        return container;
    }
    public void setContainer(BasicContentContainer container) {
        this.container = container;
    }
    public State getState() {
        return state;
    }
    public void setState(State state) {
        this.state = state;
    }

    public long getInitializationTime() {
        return initializationTime;
    }
    public void setInitializationTime(long initializationTime) {
        this.initializationTime = initializationTime;
    }
    public long getStartTime() {
        return startTime;
    }
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public long getPauseTime() {
        return pauseTime;
    }
    public void setPauseTime(long pauseTime) {
        this.pauseTime = pauseTime;
    }
    public long getStopTime() {
        return stopTime;
    }
    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }
    public long getDispositionStartTime() {
        return dispositionStartTime;
    }
    public void setDispositionStartTime(long dispositionStartTime) {
        this.dispositionStartTime = dispositionStartTime;
    }
    public long getDispositionCompletionTime() {
        return dispositionCompletionTime;
    }
    public void setDispositionCompletionTime(long dispositionCompletionTime) {
        this.dispositionCompletionTime = dispositionCompletionTime;
    }

    public ContentParser (){
    }
    public ContentParser (File file){
        this.file = file;
    }
    public ContentParser (BasicContentContainer container){
        this.container = container;
    }

    @Override
    public void run() {
        
    }

}
