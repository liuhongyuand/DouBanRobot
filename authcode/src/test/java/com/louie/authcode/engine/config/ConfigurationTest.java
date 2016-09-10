package com.louie.authcode.engine.config;

import com.louie.authcode.engine.EngineConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by liuhongyu.louie on 2016/9/9.
 */
public class ConfigurationTest {

    @Before
    public void init() {
        EngineConfiguration.defaultConfiguration();
    }

    @Test
    public void testRegex(){
        String testReplaceToNull = "q..we.e00";
        testReplaceToNull = testReplaceToNull.replaceAll(EngineConfiguration.getService().getReplaceToNullString(), "");
        testReplaceToNull = testReplaceToNull.replaceAll(EngineConfiguration.getService().getIgnoredString(), "");
        Assert.assertEquals("qwee", testReplaceToNull);
    }

}
