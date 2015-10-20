package com.qualcomm.ftcrobotcontroller.hardware;

import android.util.Log;

import com.qualcomm.ftcrobotcontroller.opmodes.PushBotAuto;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Admin on 10/5/2015.
 */
public abstract class HardwareInterface {

    public abstract void init(OpMode mode);

    public void loop(long nsSinceLastLoop) { }

    public void log(String msg) {
        Log.i(this.getClass().toString(), msg);
    }

}
