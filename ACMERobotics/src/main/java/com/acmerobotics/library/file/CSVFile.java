package com.acmerobotics.library.file;

import android.content.Context;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import java.io.IOException;
import java.lang.reflect.Field;

public class CSVFile extends DataFile {

    private Class itemClass;
    private Field[] fields;

    public CSVFile(OpMode ctx, String filename, Class c) {
        super(ctx, filename);

        itemClass = c;
        fields = c.getFields();
    }

    private String join(String[] arr, String delimiter) {
        String out = arr[0];
        for (int i = 1; i < arr.length; i++) {
            out += delimiter + arr[i];
        }
        return out;
    }

    private void writeHeader() {
        String[] names = new String[fields.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = fields[i].getName();
        }
        try {
            writer.write(join(names, ",") + "\n");
        } catch (IOException e) {
            RobotLog.e(e.getMessage());
        }
    }

    public void write(Object o) {
        String[] values = new String[fields.length];
        try {
            for (int i = 0; i < fields.length; i++) {
                String value = (String) fields[i].get(o);
                values[i] = value;
            }
            writer.write(join(values, ",") + "\n");
        } catch (Exception e) {
            RobotLog.e(e.getMessage());
        }
    }

}
