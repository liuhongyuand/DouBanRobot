package com.louie.authcode.engine.core.cut.v2.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by liuhongyu.louie on 2016/9/3.
 */
public class ScanUtilTest {
    private static final int[][] TEST_IMAGE = {{
            -1,
            -1,
            -1,
             1,
             1,
             1,
            -1,
            -1,
            -1
    },{
            -1,
             1,
             1,
            -1,
            -1,
            -1,
             1,
            -1,
            -1
    },{
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1
    }};

    @Test
    public void testContinuouslyBlackPoint(){
        Assert.assertEquals(true, ScanUtil.isContinuouslyBlackPoint(TEST_IMAGE, 0));
    }

    @Test
    public void testDiscontinuouslyBlackPoint(){
        Assert.assertEquals(false, ScanUtil.isContinuouslyBlackPoint(TEST_IMAGE, 1));
    }

    @Test
    public void testContinuouslyWhitePoint(){
        Assert.assertEquals(true, ScanUtil.isContinuouslyBlackPoint(TEST_IMAGE, 2));
    }

}
