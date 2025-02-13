package com.tarum.io.parser;

import com.tarum.util.GlueList;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;

public class FileParser {

    public static final Object loadObjectFromFile (File file, Object target){
        if (file == null || !file.exists() || target == null) return null;

        Object result = loadObjectFromFile(file);

        for (Field field : result.getClass().getDeclaredFields()){
            field.setAccessible(true);
            try {
                if (target.getClass().getDeclaredField(field.getName()) != null){
                    field.set(target, field.get(result));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
    public static final Object loadObjectFromFile (File file){
        if (file == null || !file.exists()) return null;

        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Object result = null;

        try{
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            result = ois.readObject();
        } catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (ois != null){
                try {
                    ois.close();
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            return result;
        }
    }

    /**
     * TODO: COMPLETE FUNCTIONALITY ********
     * @param file
     * @return
     */
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
