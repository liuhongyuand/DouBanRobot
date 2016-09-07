package com.louie.authcode.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Utils for XML files
 * Created by liuhongyu.louie on 2016/7/19.
 */
public class ImportFileUtils {

    private static final Properties propertiesCodeIdentify = new Properties();
    private static final Logger LOG = LoggerFactory.getLogger(ImportFileUtils.class);

    static {
        initHEAD();
    }

    public static void initHEAD(){
        try {
            propertiesCodeIdentify.load(new FileReader(new File(System.getProperties().getProperty("user.dir") + "/authcode/conf/IdentifyParameters.properties")));
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public static Properties getPropertiesCodeIdentify(){
        return propertiesCodeIdentify;
    }

}
