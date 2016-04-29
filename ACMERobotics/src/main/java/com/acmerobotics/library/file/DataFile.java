package com.acmerobotics.library.file;

import android.content.Context;

import com.acmerobotics.library.R;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DataFile {

    private String filename;

    private Context appContext;
    private File file;
    protected BufferedReader reader;
    protected BufferedWriter writer;

    public DataFile(OpMode ctx, String filename) {
        this.filename = filename;
        appContext = ctx.hardwareMap.appContext;
        openFileStreams(appContext, filename);
    }

    private File getFileHandle(Context ctx, String filename) {
        File dir = new File(appContext.getExternalFilesDir(null), appContext.getString(R.string.files_directory));
        dir.mkdir();
        return new File(dir, filename);
    }

    private void openFileStreams(Context ctx, String filename) {
        file = getFileHandle(ctx, filename);
        try {
            file.createNewFile();
            reader = new BufferedReader(new FileReader(file));
            writer = new BufferedWriter(new FileWriter(file));
        } catch (Exception e) {
            RobotLog.e(e.getMessage());
        }
    }

    public BufferedReader getReader() {
        return reader;
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public void close() {
        try {
            reader.close();
            writer.close();
        } catch (IOException e) {
            RobotLog.e(e.getMessage());
        }
    }
}
