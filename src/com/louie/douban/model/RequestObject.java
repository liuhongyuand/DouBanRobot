package com.louie.douban.model;

import com.louie.douban.util.GlobalCollections;

import java.util.HashMap;
import java.util.Map;

/**
 * Package the object of request
 * Created by liuhongyu.louie on 2016/7/19.
 */
public class RequestObject {

    class Head {
        private String HOST;
        private String Connection;
        private String CacheControl;
        private String UpgradeInsecureRequests;
        private String UserAgent;
        private String Accept;
        private String Referer;
        private String AcceptEncoding;
        private String AcceptLanguage;
        private String Cookie;

        public String getHOST() {
            return HOST;
        }

        public void setHOST(String HOST) {
            this.HOST = HOST;
            GlobalCollections.HEAD.put("HOST", HOST);
        }

        public String getConnection() {
            return Connection;
        }

        public void setConnection(String connection) {
            Connection = connection;
        }

        public String getCacheControl() {
            return CacheControl;
        }

        public void setCacheControl(String cacheControl) {
            CacheControl = cacheControl;
        }

        public String getUpgradeInsecureRequests() {
            return UpgradeInsecureRequests;
        }

        public void setUpgradeInsecureRequests(String upgradeInsecureRequests) {
            UpgradeInsecureRequests = upgradeInsecureRequests;
        }

        public String getUserAgent() {
            return UserAgent;
        }

        public void setUserAgent(String userAgent) {
            UserAgent = userAgent;
        }

        public String getAccept() {
            return Accept;
        }

        public void setAccept(String accept) {
            Accept = accept;
        }

        public String getReferer() {
            return Referer;
        }

        public void setReferer(String referer) {
            Referer = referer;
        }

        public String getAcceptEncoding() {
            return AcceptEncoding;
        }

        public void setAcceptEncoding(String acceptEncoding) {
            AcceptEncoding = acceptEncoding;
        }

        public String getAcceptLanguage() {
            return AcceptLanguage;
        }

        public void setAcceptLanguage(String acceptLanguage) {
            AcceptLanguage = acceptLanguage;
        }

        public String getCookie() {
            return Cookie;
        }

        public void setCookie(String cookie) {
            Cookie = cookie;
        }
    }

    static class LoginPOST{
        private final static String source = "index_nav";
        private String form_email;
        private String form_password;
        private final static String remember = "on";

        public static String getSource() {
            return source;
        }

        public String getForm_email() {
            return form_email;
        }

        public void setForm_email(String form_email) {
            this.form_email = form_email;
        }

        public String getForm_password() {
            return form_password;
        }

        public void setForm_password(String form_password) {
            this.form_password = form_password;
        }

        public static String getRemember() {
            return remember;
        }
    }

}
