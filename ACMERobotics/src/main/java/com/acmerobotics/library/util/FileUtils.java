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

    public static String getAssetFileContents(Context ctx, String fileName) {
        String contents = null;
        AssetManager manager = ctx.getAssets();
        try {
            InputStream inputStream = manager.open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            RobotLog.e(e.getMessage());
        }
        return contents;
    }

}
