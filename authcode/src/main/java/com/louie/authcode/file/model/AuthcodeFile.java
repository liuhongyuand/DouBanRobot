package com.louie.authcode.file.model;

import com.louie.authcode.exception.ParameterException;

import java.io.File;

/**
 * Created by liuhongyu.louie on 2016/9/10.
 */
public class AuthcodeFile {

    private String url;
    private String storagePath;
    private String authcode;
    private File file;

    public AuthcodeFile(){

    }

    public AuthcodeFile(String url){
        setUrl(url);
    }

    public AuthcodeFile(String url, String authcode){
        setUrl(url);
        setAuthcode(authcode);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public String getAuthcode() {
        return authcode;
    }

    public void setAuthcode(String authcode) {
        this.authcode = authcode;
    }

    public File getFile() {
        if (file == null){
            throw new ParameterException("File is null");
        }
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
