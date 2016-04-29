package com.acmerobotics.library.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.qualcomm.robotcore.util.RobotLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Admin on 4/22/2016.
 */
public class FileUtils {

    public static String getInputStreamContents(InputStream stream) {
        StringBuilder content = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            RobotLog.e(e.getMessage());
        } finally {
            return content.toString();
        }
    }

}
