package com.qualcomm.ftcrobotcontroller.util;

/**
 * Created by Ryan on 11/23/2015.
 */
public class Helper {

    public static String byteArrayToString(byte[] arr) {
        String s = "";
        for (int i = 0; i < arr.length; i++) {
            s += String.format("%02x ", arr[i]);
        }
        return s;
    }

}
