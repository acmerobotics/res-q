package com.qualcomm.ftcrobotcontroller.util;

import com.qualcomm.robotcore.util.RobotLog;

/**
 * Created by Ryan on 11/23/2015.
 */
public class Helper {

    public static final boolean DEBUG = true;

    public static String byteArrayToString(byte[] arr) {
        String s = "";
        for (int i = 0; i < arr.length; i++) {
            s += String.format("%02x ", arr[i]);
        }
        return s;
    }

    public static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
            RobotLog.e(e.getClass() + ": " + e.getMessage());
        }
    }

}
