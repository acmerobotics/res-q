package com.acmerobotics.library.file;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFile extends DataFile {

    public LogFile(OpMode ctx, String filename) {
        super(ctx, filename);
    }

    private String getTimeString() {
        DateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");
        Date date = new Date();
        return format.format(date);
    }

    public void i(String tag, String body) {
        String head = "I/" + tag + "(" + getTimeString() + "): ";
        try {
            writer.write(head + body + "\n");
            writer.flush();
        } catch (Exception e) {
            RobotLog.e(e.getMessage());
        }
    }

}
