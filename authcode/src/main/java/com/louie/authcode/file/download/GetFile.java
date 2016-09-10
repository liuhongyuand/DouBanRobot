package com.louie.authcode.file.download;

import com.louie.authcode.file.FileService;
import com.louie.authcode.file.FileServiceImpl;
import com.louie.authcode.file.model.AuthcodeFile;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by liuhongyu.louie on 2016/9/10.
 */
public class GetFile {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetFile.class);

    public static AuthcodeFile getFile(AuthcodeFile authcodeFile) {
        try {
            Connection.Response resultImageResponse = Jsoup.connect(authcodeFile.getUrl()).ignoreContentType(true).execute();
            File file = new File(authcodeFile.getStoragePath() + "/" + authcodeFile.getAuthcode() + UUID.randomUUID() + ".jpg");
            FileOutputStream out = (new FileOutputStream(file));
            out.write(resultImageResponse.bodyAsBytes());
            out.close();
            authcodeFile.setFile(file);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return authcodeFile;
    }

}
