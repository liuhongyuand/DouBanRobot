package com.louie.authcode.file;

import com.louie.authcode.file.model.AuthcodeFile;

import java.io.File;

/**
 * Created by liuhongyu.louie on 2016/9/10.
 */
public interface FileService {

    AuthcodeFile downloadFile(AuthcodeFile file);

    AuthcodeFile delete(AuthcodeFile file);

}
