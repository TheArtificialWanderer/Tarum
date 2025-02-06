package com.tarum.io.content;

import com.tarum.app.Application;
import com.tarum.io.content.type.*;
import com.tarum.io.parser.XMLParser;
import com.tarum.util.GlueList;
import com.tarum.util.Logger;
import org.w3c.dom.*;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ContentManager {

    private Application application;
    private String directory = DEFAULT_DIRECTORY;
    private String contentContainerDirectory = DEFAULT_CONTENT_CONTAINER_DIRECTORY;
    private String configDirectory = DEFAULT_CONFIG_DIRECTORY;

    private Logger logger;

    private DefinitionManager definitionManager;

//    private ContentFormat defaultContentFormatType;

    public static final String DEFAULT_DIRECTORY = System.getenv("APPDATA")+"/.Tarum/content/";
    public static final String DEFAULT_CONTENT_CONTAINER_DIRECTORY = DEFAULT_DIRECTORY+"containers/";
    public static final String DEFAULT_CONFIG_DIRECTORY = System.getenv("APPDATA") + "/.Tarum/content/config/";
    private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

    public Application getApplication(){
        return this.application;
    }
    public void setApplication (Application application){
        this.application = application;
    }
    public String getDirectory() {
        return directory;
    }
    public void setDirectory(String directory) {
        this.directory = directory;
    }
    public String getContentContainerDirectory() {
        return contentContainerDirectory;
    }
    public void setContentContainerDirectory(String contentDirectory) {
        this.contentContainerDirectory = contentDirectory;
    }
    public String getConfigDirectory() {
        return configDirectory;
    }
    public void setConfigDirectory(String configDirectory) {
        this.configDirectory = configDirectory;
    }

    public Logger getLogger(){
        return this.logger;
    }
    public void setLogger (Logger logger){
        this.logger = logger;
    }

    public DefinitionManager getDefinitionManager() {
        return definitionManager;
    }
    public void setDefinitionManager(DefinitionManager definitionManager) {
        this.definitionManager = definitionManager;
    }

    public ContentManager(){
        this(DEFAULT_DIRECTORY);
    }
    public ContentManager (String directory){
        if (directory != null) this.directory = directory;

        initialize();
    }

    private void initialize(){
        this.definitionManager = new DefinitionManager(this);


    }

    public static boolean isWrapperType(Class<?> clazz)
    {
        return WRAPPER_TYPES.contains(clazz);
    }
    private static Set<Class<?>> getWrapperTypes() {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        return ret;
    }

    public byte[] serializeContentContainer (BasicContentContainer container){
        return null;
    }
    public String serializeContentContainerToString (BasicContentContainer container){
        return null;
    }
    public GlueList<String> serializeContentContainerToList (BasicContentContainer container){
        return null;
    }

    public GlueList<String> serializeHashMapToList (HashMap map, String valueSeparator){
        return null;
    }

    public ContentContainer loadContentContainerFile (File file){
        if (file == null || !file.exists()) return null;

        XMLParser xmlParser = new XMLParser(file);
        XMLDocument document = xmlParser.loadFile();

        if (document == null) return null;

        Document doc = document.getDocument();

        ContentContainerImpl container = new ContentContainerImpl(this, file);

        Element root = doc.getDocumentElement();
        NamedNodeMap nodeMap = root.getAttributes();

        NodeList nodeList = doc.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++){
            Node node = nodeList.item(i);
        }

        return container;
    }

    public ContentFormat loadContentFormatFile (File file){
        if (file == null || !file.exists()){
            System.out.println("ContentManager.loadContentFormatFile(File): Specified file does not exist!");
            return null;
        }

        ContentFormat format = new ContentFormat(this, file);

        XMLParser xmlParser = new XMLParser (file);

        if (!xmlParser.isValidXMLFile(file)){
        }

        XMLDocument xmlDocument = xmlParser.loadFile();

        if (xmlDocument == null) {
            if (getLogger() != null) {
                getLogger().logError("Failed to load XML document (file_path: " + file.getAbsolutePath() + ")!");
            }

            return format;
        }

        return format;
    }

    /**
     * TODO: IMPLEMENT FILE CONFIGURATION SETTINGS FOR VARIOUS 'CONTENT_CONTAINER' TYPE(S),
     * TODO: SET A DEFAULT FILE TYPE (E.X XML) FOR 'CONTENT_CONTAINER' TYPE(S)
     * @param container The specifiable object instance being exported to the local filesystem
     * @return
     */
    public boolean performContentExportation (BasicContentContainer container){
        return performContentExportation(container, container.getFile());
    }
    public boolean performContentExportation (BasicContentContainer container, File file){
        if (file != null){
            container.setFile(file);
        }

        Class parentClass = container.getClass().getSuperclass();
        Field[] fields = parentClass.getDeclaredFields();

        for (Field field : fields){
            field.setAccessible(true);
            Object value = null;

            try {
                value = field.get(container);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(container.getFile());
            fos.write(container.getContent());
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

        return container.getFile().exists();
    }

}
