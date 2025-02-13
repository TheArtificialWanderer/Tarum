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
    private String name = "BasicContentContainer";
    private String filePath;
    private String fileExtension = DEFAULT_FILE_EXTENSION;
    private File file;
    private ContentFormat.Type contentFormatType = ContentFormat.Type.XML_DOCUMENT;

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
    public String getFilePath(){
        return this.filePath;
    }
    public void setFilePath (String filePath){
        this.filePath = filePath;
    }
    public String getFileExtension(){
        return this.fileExtension;
    }
    public void setFileExtension (String fileExtension){
        this.fileExtension = fileExtension;
    }
    public File getFile() {
        return new File (filePath);
    }
    public void setFile(File file) {
        this.filePath = file.getAbsolutePath();
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
    public ContentFormat.Type getContentFormatType(){
        return this.contentFormatType;
    }
    /**
     * TODO: ******
     */
    public void setContentFormat (ContentFormat.Type type){
        this.contentFormatType = type;
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
        if (file != null){
            this.file = file;
        } else {
            this.file = generateOutputFile();
        }
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
        String fileName = null;

        if (getName() == null){
            fileName = this.getClass().getSuperclass().getSimpleName();
            setName(fileName);
        } else {
            fileName = getName();
        }

        if (getFileExtension() == null){
            setFileExtension(DEFAULT_FILE_EXTENSION);
        }

        if (!dir.exists()){
            dir.mkdirs();
        } else {
            int fileCount = dir.listFiles().length;
            filePath = dir.getAbsolutePath() + "/" + fileName;
            if (fileCount > 0){
                filePath += "_";
            }
            filePath += getFileExtension();
        }
        return this.file = new File (filePath);
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

        if (!XMLParser.isValidXMLFile(file)){
            // TODO: THE SPECIFIED FILE IS NOT A VALID XML FILE, ATTEMPT TO LOAD
            // TODO: THE FILE CONTENT USING AN ALTERNATIVE THAN THE XMLPARSER CLASS.
            return true;
        }

        XMLDocument xmlDocument = new XMLParser(file).loadFile();

        if (xmlDocument == null) return false;

        if (xmlDocument.prepareObject(this)){
            // Successfully loaded the content container contents from the
            // specified XML document file.
            return true;
        } else {
            Class parentClass = this.getClass().getSuperclass();
            Field[] fields = parentClass.getDeclaredFields();

            for (Field field : fields){
                field.setAccessible(true);

                try {
                    Object val = field.get(this);
//                    field.set(this, val);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            return true;
        }
    }

    /**
     * TODO: IMPLEMENT OPTIONAL XML PARSING FOR CONTENT EXPORTATION
     */
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
