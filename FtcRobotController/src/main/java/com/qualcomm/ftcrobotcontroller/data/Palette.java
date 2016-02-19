package com.qualcomm.ftcrobotcontroller.data;

import android.graphics.Color;

/**
 * Created by Admin on 2/16/2016.
 */
public class Palette {

    public enum Color {
        RED,
        YELLOW,
        GREEN,
        CYAN,
        BLUE,
        VIOLET
    }

    public static Color getColorFromHSV(float[] hsv) {
        float hue = hsv[0];
        if (hue < 30) {
            return Color.RED;
        } else if (hue < 90) {
            return Color.YELLOW;
        } else if (hue < 150) {
            return Color.GREEN;
        } else if (hue < 210) {
            return Color.CYAN;
        } else if (hue < 270) {
            return Color.BLUE;
        } else if (hue < 330) {
            return Color.VIOLET;
        } else {
            return Color.RED;
        }
    }

    public static Color getColorFromRGB(int r, int g, int b) {
        float[] hsv = new float[3];
        android.graphics.Color.RGBToHSV(r, g, b, hsv);
        return getColorFromHSV(hsv);
    }

}
