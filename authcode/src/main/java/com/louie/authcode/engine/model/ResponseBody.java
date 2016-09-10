package com.louie.authcode.engine.model;

/**
 * Created by liuhongyu.louie on 2016/9/9.
 */
public class ResponseBody extends AbstractResponse implements Response {

    private String code = "0";
    private String message = "";
    private String authcode = "";

    public ResponseBody(){

    }

    public ResponseBody(String code, String message){
        setCode(code);
        setMessage(message);
    }

    public ResponseBody(String code, String message, String authcode){
        setCode(code);
        setMessage(message);
        setAuthcode(authcode);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthcode() {
        return authcode;
    }

    public void setAuthcode(String authcode) {
        this.authcode = authcode;
    }
}
