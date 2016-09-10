package com.louie.authcode.engine.config;

import com.louie.authcode.engine.brain.Identify.IdentificationService;
import com.louie.authcode.engine.brain.Identify.knn.IdentificationServiceKNNImpl;
import com.louie.authcode.engine.core.color.BinaryValue;
import com.louie.authcode.engine.core.color.ColorProcessService;
import com.louie.authcode.engine.core.cut.CharCutService;
import com.louie.authcode.engine.core.cut.v2.DivideProcess;
import com.louie.authcode.engine.core.noise.NoiseProcessService;
import com.louie.authcode.engine.core.noise.PointNoiseScan;

/**
 * The configuration of the engine.
 * Created by liuhongyu.louie on 2016/9/7.
 */
public class Configuration implements ConfigurationService {

    private ColorProcessService colorProcessService = new BinaryValue();
    private NoiseProcessService noiseProcessService = new PointNoiseScan();
    private CharCutService charCutService = new DivideProcess();
    private IdentificationService identificationService = new IdentificationServiceKNNImpl();
    private final String IGNORED_LETTER_REGEX = "\\.";
    private final String REPLACE_TO_NULL_REGEX = "\\d";
    private boolean enabledImportData = true;

    public void setColorProcessService(ColorProcessService colorProcessService) {
        this.colorProcessService = colorProcessService;
    }

    public void setNoiseProcessService(NoiseProcessService noiseProcessService) {
        this.noiseProcessService = noiseProcessService;
    }

    public void setCharCutService(CharCutService charCutService) {
        this.charCutService = charCutService;
    }

    public void setIdentificationService(IdentificationService identificationService) {
        this.identificationService = identificationService;
    }

    @Override
    public ColorProcessService getColorProcessService() {
        return colorProcessService;
    }

    @Override
    public NoiseProcessService getNoiseProcessService() {
        return noiseProcessService;
    }

    @Override
    public CharCutService getCharCutService() {
        return charCutService;
    }

    @Override
    public IdentificationService getIdentificationService() {
        return identificationService;
    }

    @Override
    public boolean getEnabledImportData() {
        return enabledImportData;
    }

    @Override
    public String getIgnoredString() {
        return IGNORED_LETTER_REGEX;
    }

    @Override
    public String getReplaceToNullString() {
        return REPLACE_TO_NULL_REGEX;
    }

    @Override
    public int getMaxMatrix() {
        return 13;
    }

    @Override
    public double getSimilarity() {
        return EngineParameters.similarity;
    }

    @Override
    public int getTargetColor() {
        return EngineParameters.target;
    }

    @Override
    public int getColorDifferentRate() {
        return EngineParameters.difRate;
    }

    @Override
    public int getNearby() {
        return EngineParameters.nearby;
    }

    @Override
    public double getDeviation() {
        return EngineParameters.deviation;
    }
}
