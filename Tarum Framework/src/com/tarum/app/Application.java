package com.tarum.app;

import com.tarum.io.Config;
import com.tarum.io.content.ContentManager;
import com.tarum.util.Logger;

import java.io.File;

public class Application implements Runnable{

    private Config config;
    private ContentManager contentManager;
    private Logger logger;

    private String[] args;

    private boolean running;

    public Config getConfig() {
        return config;
    }
    public void setConfig(Config config) {
        this.config = config;
    }
    public ContentManager getContentManager() {
        return contentManager;
    }
    public void setContentManager(ContentManager contentManager) {
        this.contentManager = contentManager;
    }
    public Logger getLogger() {
        return logger;
    }
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public String[] getArgs() {
        return args;
    }
    public void setArgs(String[] args) {
        this.args = args;
    }
    public boolean isRunning() {
        return running;
    }
    public void setRunning(boolean running) {
        this.running = running;
    }

    public Application(){
        this(null);
    }
    public Application (String[] args){
        this.args = args;
    }

    @Override
    public void run() {
        onStart();

        initialize();

        while (isRunning()){

        }
    }

    public void initialize(){
        this.logger = new Logger();
        logger.start();

        logger.logLine("Initializing application..");

        onInitializationStarted();

        contentManager = new ContentManager();

        File configFile = new File(System.getenv("APPDATA")+"/.Tarum/", "application.cfg");
        this.config = new Config(configFile);

        if (config.load()){
            logger.logLine("Successfully loaded application configuration settings! " +
                    "(file_path: " + configFile.getAbsolutePath() + ")");
        }

        this.running = true;

        onInitializationCompleted();
    }

    public void onStart(){
    }
    public void onInitializationStarted(){
    }
    public void onInitializationCompleted(){
    }
    public void onPause(){
    }
    public void onStop(){
    }
    public void onDispositionStarted(){
    }

}
