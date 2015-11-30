package com.qualcomm.ftcrobotcontroller.hardware;

import android.graphics.LinearGradient;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import java.util.HashMap;

/**
 * Created by Admin on 10/5/2015.
 */


public abstract class RobotController extends OpMode {
    private HashMap<String, HardwareInterface> hardwareInterfaces;
    private long nsLastLoop;

    public void init() {
        hardwareInterfaces = new HashMap<String, HardwareInterface>();
        nsLastLoop = System.currentTimeMillis();

        RobotLog.clearGlobalErrorMsg();
        RobotLog.d("==============Robot Controller============");
    }

    public boolean registerHardwareInterface(String name, HardwareInterface hi) {
        if (hardwareInterfaces.containsKey(name)) {
            return false;
        } else {
            hardwareInterfaces.put(name, hi);
            hi.init(this);
            return true;
        }
    }

    public boolean deregisterHardwareInterface(String name, HardwareInterface hi) {
        if (hardwareInterfaces.containsKey(name)) {
            hardwareInterfaces.remove(name);
            return true;
        } else {
            return false;
        }
    }

    public void loop() {
        for (String name : hardwareInterfaces.keySet()) {
            hardwareInterfaces.get(name).loop(getRuntime());
        }
        resetStartTime();
    }
}
