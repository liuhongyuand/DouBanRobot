package com.louie.authcode.utils;

import com.louie.authcode.StartService;
import com.louie.authcode.file.model.AuthcodeFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by liuhongyu.louie on 2016/9/10.
 */
public class FileDeleteUtil {

    public static final Queue<AuthcodeFile> fileQueue = new LinkedBlockingQueue<>();
    private static final Set<AuthcodeFile> failDeleteFile = new HashSet<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(FileDeleteUtil.class);

    static {
        startWatch();
        startDeleteFailWatch();
    }

    private static void startWatch(){
        ThreadSupport.threadPool.execute(() -> {
            LOGGER.info("File delete watcher has been start.");
            while (StartService.SystemIsOnline) {
                AuthcodeFile file = fileQueue.peek();
                if (file != null){
                    if (file.getFile() != null && file.getFile().exists()) {
                        if (!file.getFile().delete()) {
                            failDeleteFile.add(file);
                        }
                    }
                    fileQueue.poll();
                    LOGGER.info("Delete " + file.getFile().getName());
                    TimeUtil.sleep(100);
                } else {
                    TimeUtil.sleep(10 * 1000);
                }
            }
        });
    }

    private static void startDeleteFailWatch(){
        ThreadSupport.threadPool.execute(() -> {
            LOGGER.info("File delete failed watcher has been start.");
            while (StartService.SystemIsOnline) {
                List<AuthcodeFile> removeFiles = new LinkedList<>();
                int startSize = failDeleteFile.size();
                failDeleteFile.forEach((file) -> {
                    if (file.getFile().delete()){
                        removeFiles.add(file);
                    }
                    TimeUtil.sleep(100);
                });
                failDeleteFile.removeAll(removeFiles);
                LOGGER.info("Delete " + removeFiles.size() + " failed files, remainder " + (failDeleteFile.size() - startSize) + " files.");
                TimeUtil.sleep(60 * 1000);
            }
        });
    }

}
