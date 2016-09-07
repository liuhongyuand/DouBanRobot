package com.louie.authcode.engine;

import com.louie.authcode.engine.config.Configuration;
import com.louie.authcode.engine.config.ConfigurationService;
import com.louie.authcode.exception.InitException;

/**
 * Created by liuhongyu.louie on 2016/9/7.
 */
public class EngineConfiguration {

    private static ConfigurationService configurationService;

    private EngineConfiguration(){
    }

    public static void loadConfiguration(final ConfigurationService ConfigurationService){
        configurationService = ConfigurationService;
    }

    public static void defaultConfiguration(){
        loadConfiguration(new Configuration());
    }

    public static ConfigurationService getService(){
        if (configurationService == null){
            defaultConfiguration();
        }
        return configurationService;
    }

}
