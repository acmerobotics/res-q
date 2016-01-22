package com.qualcomm.ftcrobotcontroller.hardware;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Admin on 10/5/2015.
 */
public abstract class HardwareInterface {

    public abstract void init(OpMode mode);

    public void loop(double timeSinceLastLoop) { }

    public String getStatusString() { return "unimplemented"; }

    public void logcat(String msg) {
        Log.i(this.getClass().toString(), msg);
    }

}
