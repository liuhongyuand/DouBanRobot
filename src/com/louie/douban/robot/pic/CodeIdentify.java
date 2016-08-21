package com.louie.douban.robot.pic;

import com.louie.douban.robot.services.AbstractPicProcess;
import com.louie.douban.robot.services.CodeImport;
import com.louie.douban.util.ImportFileUtils;
import com.louie.douban.util.Parameters;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * Created by liuhongyu.louie on 2016/8/21.
 */
public class CodeIdentify {
    private final String FILE = Parameters.PATH + "/training/30955624-d6e8-4d4e-95d0-b923a38c2d04.jpg";

    public void trainingPicIdentify(){
        AbstractPicProcess process = new CodeImport();
        JFrame frame = new JFrame();
        frame.setLayout(null);
        frame.setSize(600, 100);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Object[] results = process.process(FILE);
        List<?> letters = (List<?>) results[0];
        Set<?> buffers = (Set<?>) results[1];
        int temp = 10;
        for (Object bufferObj : buffers) {
            BufferedImage buffer = (BufferedImage) bufferObj;
            JLabel label = new JLabel("");
            label.setIcon(new ImageIcon(buffer));
            label.setBounds(temp, 0, buffer.getWidth(), buffer.getHeight());
            frame.add(label);
            temp = temp + buffer.getWidth() + 20;
        }
        frame.setVisible(true);
    }

    public static void main(String[] args){
        new CodeIdentify().trainingPicIdentify();
    }

}
