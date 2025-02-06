import com.tarum.app.Application;
import com.tarum.io.content.type.ContentMap;
import com.tarum.io.content.DatabaseManager;
import com.tarum.io.content.DefinitionManager;
import com.tarum.util.Logger;

import java.io.File;

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
        contentMap.setName ("content_map_test");
        contentMap.setFile(new File (contentMap.getContentManager().getContentContainerDirectory()+"content_map/", "content_map_test.dat"));

        long entryKey = contentMap.add("Test_Object");

        // Assign a secondary key to the new entry so that we may locate the
        // entry by String value as well as by Long value
        contentMap.addKey(entryKey, "Test_Object_Key");

        logger.logLine ("Created new 'ContentMap' instance..");

        if (contentMap.export()){
            logger.logLine ("Successfully exported 'ContentMap' instance to the local filesystem! " +
                    "(file_path: " + contentMap.getFile().getAbsolutePath() + ")");
        } else {
            logger.logError("Failed to export 'ContentMap' instance to the local filesystem! (file_path:" +
                    contentMap.getFile().getAbsolutePath() + ")");
        }

    }

    public static void main(String[] args) {
        Main main = new Main();

        Thread t = new Thread (main);
        t.start();
    }

}