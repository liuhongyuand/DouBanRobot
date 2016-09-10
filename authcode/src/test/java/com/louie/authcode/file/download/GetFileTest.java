package com.louie.authcode.file.download;

import com.louie.authcode.file.FileService;
import com.louie.authcode.file.FileServiceImpl;
import com.louie.authcode.file.model.AuthcodeFile;
import org.junit.Test;

/**
 * Created by liuhongyu.louie on 2016/9/10.
 */
public class GetFileTest {

    @Test
    public void testGetFile(){
        String url = "http://www.educity.cn/wenda/images/wd_logo.jpg";
        String authcode = "test0000001";
        FileService service = new FileServiceImpl();
        AuthcodeFile file = new AuthcodeFile(url, authcode);
        service.downloadFile(file);
    }

}
