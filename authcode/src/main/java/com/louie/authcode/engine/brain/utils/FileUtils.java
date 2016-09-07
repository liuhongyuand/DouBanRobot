package com.louie.authcode.engine.brain.utils;

import com.louie.authcode.engine.config.EngineParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liuhongyu.louie on 2016/8/21.
 */
public class FileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    public static void ObjectToFile(Object object){
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        File backFile = new File(EngineParameters.CodeIdentifyDataPath + "_" + dataFormat.format(new Date()));
        try(ObjectOutputStream objectOutputStream =  new ObjectOutputStream(new FileOutputStream(EngineParameters.CodeIdentifyDataPath));
            ObjectOutputStream objectOutputStreamBack =  new ObjectOutputStream(new FileOutputStream(backFile))){
            objectOutputStream.writeObject(object);
            objectOutputStreamBack.writeObject(object);
        }catch(Exception e){
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static Object FileToObject(){
        Object object = null;
        if (new File(EngineParameters.CodeIdentifyDataPath).length() == 0) {
            return null;
        }
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(EngineParameters.CodeIdentifyDataPath))){
            object = objectInputStream.readObject();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ClassNotFoundException ce){
            LOGGER.info("Try to get object from file, but failed");
        }
        return object;
    }

}
