package com.tarum.io.content.type;

import com.tarum.io.content.ContentManager;
import com.tarum.io.parser.XMLParser;
import com.tarum.util.MathUtils;

import java.io.*;
import java.lang.reflect.Field;

public abstract class BasicContentContainer implements Serializable {

    private transient ContentManager contentManager;

    private long uid;
    private int id;
    private String name;
    private String filePath;

    private int contentLength;
    private byte[] content;

    public static final String DEFAULT_FILE_EXTENSION = ".dat";

    public ContentManager getContentManager() {
        return contentManager;
    }
    public void setContentManager(ContentManager contentManager) {
        this.contentManager = contentManager;
    }
    public long getUID() {
        return uid;
    }
    public void setUID(long uid) {
        this.uid = uid;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public File getFile() {
        return new File (filePath);
    }
    public void setFile(File file) {
        this.filePath = file.getAbsolutePath();
    }
    public void setFilePath (String filePath){
        this.filePath = filePath;
    }
    public int getContentLength() {
        return contentLength;
    }
    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }
    public byte[] getContent() {
        return content;
    }
    public void setContent(byte[] content) {
        this.content = content;
    }

    public BasicContentContainer (){
        this(new ContentManager());
    }
    public BasicContentContainer(ContentManager contentManager){
        this(contentManager, null);
    }
    public BasicContentContainer(ContentManager contentManager, File file){
        this.contentManager = contentManager;
        this.filePath = file != null ? file.getAbsolutePath() : generateOutputFile().getAbsolutePath();
        this.uid = MathUtils.GenerateUID();
    }
    public BasicContentContainer(File file){
        this(new ContentManager(), file);
        this.uid = MathUtils.GenerateUID();
    }

    public File generateOutputFile(){
        Class superClass = this.getClass().getSuperclass();
        String parentClass = null;
        String dirPath = getContentManager().getContentContainerDirectory();

        if (superClass != null){
            parentClass = this.getClass().getSuperclass().getSimpleName();
        }
        if (parentClass != null){
            dirPath += parentClass +"/";
        }

        File dir = new File (dirPath);

        String filePath = dir.getAbsolutePath();

        if (!dir.exists()){
            dir.mkdirs();
        } else {
            int fileCount = dir.listFiles().length;
            filePath = dir.getAbsolutePath() + "/" + this.getClass().getSimpleName();
            if (fileCount > 0){
                filePath += "_";
            }
            filePath += DEFAULT_FILE_EXTENSION;
        }
        return new File (filePath);
    }

    public boolean load(){
        return loadFile(getFile());
    }
    public boolean loadFile (File file){
        if (file != null && file.exists()){
            setFile(file);
        }
        return performContentImportation(getFile());
    }
    public boolean performContentImportation (File file){
        if (file == null || !file.exists()) return false;

        XMLDocument xmlDocument = new XMLParser(file).loadFile();

        if (xmlDocument == null) return false;

        Field[] fields = null;


        return true;
    }

    public boolean export(){
        return export(getFile());
    }
    public boolean export (File file){
        return performContentExportation(file);
    }
    public boolean performContentExportation(File file){
        if (file != null){
            setFile(file);
        } else {
            if (getFile() == null){
                setFile(generateOutputFile());
            }
        }

        System.out.println ("BasicContentContainer.performContentExportation(File)");

        if (getContent() == null) {
            return false;
        }

        FileOutputStream fos = null;
        try{

            fos = new FileOutputStream(getFile());
            fos.write(content);
            fos.flush();

        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if (fos != null){
                try{
                    fos.close();
                } catch (IOException e1){
                    e1.printStackTrace();
                }
            }
        }

        return getFile().exists();
    }

}
