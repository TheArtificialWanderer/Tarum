package com.tarum.util;

import java.io.*;
import java.util.Base64;

public class IOUtils {

    public static final GlueList<File> GetAllFilesRecursively (String targetDir){
        if (targetDir == null) return null;

        GlueList<File> fileList = new GlueList<>();
        File[] files = new File (targetDir).listFiles();

        if (files == null) return fileList;

        for (File file : files){
            if (file.isDirectory()){
                GlueList<File> subFiles = GetAllFilesRecursively(file.getAbsolutePath());
                fileList.addAll(subFiles);
            } else {
                fileList.add(file);
            }
        }

        return fileList;
    }

    public static final String SerializeObjectToString (Object object){
        if (object == null) return null;

        String result = null;
        ObjectOutputStream objectOutputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try{
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if (objectOutputStream != null){
                try {
                    objectOutputStream.close();
                } catch (IOException e){
                    e.printStackTrace();
                } finally {
                    result = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
                }
            }
        }

        return result;
    }

    public static final GlueList<String> loadFileHeader (File file){
        if (file == null) return null;

        GlueList<String> fileContent = new GlueList<>();

        BufferedReader reader = null;
        String ln = null;

        try{
            reader = new BufferedReader(new FileReader(file));

            while ((ln = reader.readLine()) != null){

                //
                if (ln.replaceAll(" ", "") == "}"){
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        return fileContent;
    }

    public static final GlueList<String> LoadFileContent (File file){
        GlueList<String> result = new GlueList<>();
        if (file == null || !file.exists()) return result;

        BufferedReader reader = null;
        FileReader fileReader = null;
        String ln = null;

        try{
            fileReader = new FileReader(file);
            reader = new BufferedReader(fileReader);

            while ((ln = reader.readLine()) != null){
                result.add(ln);
            }

        } catch (IOException e){
            e.printStackTrace();
        } finally{
            try{
                if (reader != null){
                    reader.close();
                }
                if (fileReader != null){
                    fileReader.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return result;
        }
    }

}
