package com.qualcomm.ftcrobotcontroller.util;

import com.qualcomm.robotcore.util.RobotLog;

/**
 * Created by Ryan on 11/23/2015.
 */
public class Helper {

    public static final boolean DEBUG = false;

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

    public static int assembleWord(byte lsb, byte msb) {
        return (lsb & 0xff) | ((msb & 0xff) << 8); // the '& 0xff' converts from signed to unsigned byte
    }

    public static boolean compareDouble(double a, double b) {
        return Math.abs(a - b) < Math.pow(10, 6);
    }

}
