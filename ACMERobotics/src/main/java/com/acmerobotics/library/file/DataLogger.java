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
import java.io.PrintWriter;

public class DataLogger {

    private Context appContext;
    private File file;
    private FileOutputStream fos;
    private PrintWriter writer;

    public DataLogger(OpMode ctx, String filename) {
        appContext = ctx.hardwareMap.appContext;
        File dir = new File(appContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "DataLogger");
        dir.mkdir();
        file = new File(dir, filename);
        try {
            file.createNewFile();
        } catch (IOException e) {
            RobotLog.e(e.getMessage());
        }
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            RobotLog.e(e.getMessage());
        }
        writer = new PrintWriter(fos);
    }

    public void writeLine(String line) {
        writer.write(line + "\n");
        writer.flush();
    }

    public void close() {
        writer.close();
    }
}
