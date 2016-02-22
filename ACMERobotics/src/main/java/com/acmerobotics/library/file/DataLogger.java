package com.acmerobotics.library.file;

import android.content.Context;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.RobotLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class DataLogger {

    private Context appContext;
    private File file;
    private FileOutputStream fos;
    private boolean active = false;

    public DataLogger(OpMode ctx, String filename, boolean overwrite) {
        appContext = ctx.hardwareMap.appContext;
        File dir = appContext.getFilesDir();
        if (!overwrite) {
            String[] files = dir.list();
            List<String> fileList = Arrays.asList(files);
            if (fileList.contains(filename)) {
                int num = 1;
                while (fileList.contains(filename + "_" + num)) {
                    num++;
                }
                filename += "_" + num;
            }
        }
        // TODO implement real extension system
        filename += ".csv";
        try {
            fos = appContext.openFileOutput(filename, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            RobotLog.e(e.getMessage());
        }
        active = true;
    }

    public DataLogger(OpMode ctx, String filename) {
        this(ctx, filename, true);
    }

    public boolean writeLine(String line) {
        if (!active) return false;
        try {
            fos.write((line + "\n").getBytes());
        } catch (IOException e) {
            RobotLog.e(e.getMessage());
        }
        return true;
    }

    public void close() {
        active = false;
        try {
            fos.close();
        } catch (IOException e) {
            RobotLog.e(e.getMessage());
        }
    }
}
