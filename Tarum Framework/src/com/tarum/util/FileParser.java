package com.tarum.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class FileParser {

    public static final HashMap ParseFileHeader (File file){
        if (file == null || !file.exists()) return null;

        HashMap<String, String> parameterMap = new HashMap<>();

        GlueList<String> fileContent = new GlueList<>();
        BufferedReader reader = null;
        FileReader fileReader = null;
        String ln = null;
        int lnCount = 0;

        try{
            fileReader = new FileReader(file);
            reader = new BufferedReader(fileReader);

            while ((ln = reader.readLine()) != null){

                // Parse the information from the beginning line which contains the
                // file configuration parameters, it indicates to content parsers how to
                // handle the file information appropriately along with various META data
                // depending on the file architecture type.
                if (lnCount == 0){
                    if (!ln.contains("<") && !ln.contains(">")){

                    }

                    ln = ln.replace("<", "").replace(">", "");

                    String[] params = ln.split(",");

                    for (String param : params){
                        String paramName = param.split(":")[0];
                        String paramValue = param.split(":")[1];

                        parameterMap.put(paramName, paramValue);
                    }
                }

                lnCount++;
            }

        } catch (IOException e){
            e.printStackTrace();
        }

        return parameterMap;
    }

}
