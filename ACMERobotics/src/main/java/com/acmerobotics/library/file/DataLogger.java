package com.acmerobotics.library.file;

import android.content.Context;
import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.RobotLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataLogger {

    private Context appContext;
    private File file;
    private FileOutputStream fos;
    private boolean active = false;

    public DataLogger(OpMode ctx, String filename) {
        appContext = ctx.hardwareMap.appContext;
        File dir = new File(appContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "DataLogger");
        file = new File(dir, filename);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean writeLine(String line) {
        if (!active) return false;
        try {
            fos.write((line + "\n").getBytes());
            fos.flush();
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
