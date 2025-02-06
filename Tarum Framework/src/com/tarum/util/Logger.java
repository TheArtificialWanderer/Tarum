package com.tarum.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    private String rootDir;

    private File activeFile;

    private BufferedWriter writer;

    public static enum EntryFlag{
        NONE, WARNING, ERROR
    }

    public String getRootDir() {
        return rootDir;
    }
    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }
    public File getActiveFile() {
        return activeFile;
    }
    public void setActiveFile(File activeFile) {
        this.activeFile = activeFile;
    }
    public BufferedWriter getWriter() {
        return writer;
    }
    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    public Logger(){
        this(System.getenv("APPDATA")+"/.Tarum/logs/");
    }
    public Logger(String rootDir){
        this.rootDir = rootDir == null ? System.getenv("APDATA") + "/.Tarum/logs/" : rootDir;
    }

    public void start(){
        initialize();
    }

    private void initialize(){

        File dir = new File (rootDir);

        if (!dir.exists()){
            dir.mkdirs();
        }

        this.activeFile = generateOutputFile();

        try{
            writer = new BufferedWriter(new FileWriter(activeFile));
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    private File generateOutputFile(){
        File dir = new File (rootDir);

        int fileCount = dir.listFiles().length;

        String fileName = "log";

        if (fileCount > 0){
            fileName += "_" + fileCount;
        }

        fileName += ".txt";

        File result = new File (rootDir, fileName);
        return result;
    }

    public boolean log (String logEntry){
        try{
            if (writer == null) return false;

            String entry = logEntry;

            writer.write(entry);

            writer.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }
    public boolean logLine (String logEntry){
        return logLine(logEntry, EntryFlag.NONE);
    }
    public boolean logLine (String logEntry, EntryFlag entryFlag){
        try{
            if (writer == null) return false;

            String entry = null;

            if (entryFlag != null && entryFlag != EntryFlag.NONE){
                entry = "[FLAG_" + entryFlag.name() + "]: ";
            }

            if (entry == null){
                entry = logEntry + "\n";
            } else {
                entry += logEntry + "\n";
            }

            System.out.println(entry);
            writer.write(entry);
            writer.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }
    public boolean logWarning (String logEntry){
        return logLine (logEntry, EntryFlag.WARNING);
    }
    public boolean logError (String logEntry){
        return logLine (logEntry, EntryFlag.ERROR);
    }

    public void close(){
        try{
            if (writer != null){
                writer.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
