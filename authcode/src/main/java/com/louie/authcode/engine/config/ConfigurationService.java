package com.louie.authcode.engine.config;

import com.louie.authcode.engine.brain.Identify.IdentificationService;
import com.louie.authcode.engine.core.color.ColorProcessService;
import com.louie.authcode.engine.core.cut.CharCutService;
import com.louie.authcode.engine.core.noise.NoiseProcessService;

/**
 * Created by liuhongyu.louie on 2016/9/7.
 */
public interface ConfigurationService {

    ColorProcessService getColorProcessService();

    NoiseProcessService getNoiseProcessService();

    CharCutService getCharCutService();

    IdentificationService getIdentificationService();

    boolean getEnabledImportData();

    String getIgnoredString();

    String getReplaceToNullString();

    int getMaxMatrix();

    double getSimilarity();

    int getTargetColor();

    int getColorDifferentRate();

    int getNearby();

    double getDeviation();

}
