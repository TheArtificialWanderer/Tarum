import com.tarum.app.Application;
import com.tarum.io.content.type.ContentFormat;
import com.tarum.io.content.type.ContentMap;
import com.tarum.io.content.DatabaseManager;
import com.tarum.io.content.DefinitionManager;
import com.tarum.util.Logger;

import java.io.File;
import java.util.HashMap;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends Application {

    private DatabaseManager databaseManager;

    public Main(){

    }

    @Override
    public void onStart() {
        initialize();

    }

    @Override
    public void onInitializationCompleted(){
        Logger logger = getLogger();
        DefinitionManager definitionManager = getContentManager().getDefinitionManager();
        databaseManager = new DatabaseManager();

        ContentMap contentMap = new ContentMap(getContentManager());
        File contentMapFile = new File(contentMap.getContentManager().getContentContainerDirectory()+"content_map/", "content_map_test.dat");
        contentMap.setName ("content_map_test");
        contentMap.setFile(contentMapFile);

        /**
         * TODO: COMPLETE 'CONTENTFORMAT' CLASS AND IT'S IMPLEMENTATION IN THE 'CONTENTCONTAINER' CLASSES
         */
        contentMap.setContentFormat (ContentFormat.Type.XML_DOCUMENT);

        long entryKey = contentMap.add("Test_Object");
        long test = contentMap.add("Test_Object_Two");
        long test2 = contentMap.add("Test_Object_Three");

        // Assign a secondary key to the new entry so that we may locate the
        // entry by String value as well as by Long value
        contentMap.addKey(entryKey, "testObjectKey");
        contentMap.addKey(test, "testKey");
        contentMap.addKey(test2, "testKey2");

        logger.logLine ("Created new 'ContentMap' instance..");

        if (contentMap.export()){
            logger.logLine ("Successfully exported 'ContentMap' instance to the local filesystem! " +
                    "(file_path: " + contentMap.getFile().getAbsolutePath() + ")");
        } else {
            logger.logError("Failed to export 'ContentMap' instance to the local filesystem! (file_path:" +
                    contentMap.getFile().getAbsolutePath() + ")");
        }

        ContentMap loadTest = new ContentMap(contentMapFile);
        if (contentMap.load()){
            logger.logLine("Successfully loaded ContentMap from file (file_path:" + contentMapFile.getAbsolutePath() + ")!");
        } else {
            logger.logError("Failed to load ContentMap from file (file_path:" + contentMapFile.getAbsolutePath() + ")!");
        }

        HashMap dataMap = loadTest.getDataMap();

        logger.logLine("ContentMap Entry count: " + dataMap.entrySet().size());
    }

    public static void main(String[] args) {
        Main main = new Main();

        Thread t = new Thread (main);
        t.start();
    }

}