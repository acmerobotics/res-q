package com.qualcomm.ftcrobotcontroller.hardware;

import android.graphics.LinearGradient;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.HashMap;

/**
 * Created by Admin on 10/5/2015.
 */


public abstract class RobotController extends LinearOpMode {
    private HashMap<String, HardwareInterface> hardwareInterfaces;
    private long nsLastLoop;

    public void begin() {
        hardwareInterfaces = new HashMap<String, HardwareInterface>();
        nsLastLoop = System.currentTimeMillis();
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

    public boolean unregisterHardwareInterface(String name, HardwareInterface hi) {
        if (hardwareInterfaces.containsKey(name)) {
            hardwareInterfaces.remove(name);
            return true;
        } else {
            return false;
        }
    }

    public void update() {
        for (String name : hardwareInterfaces.keySet()) {
            hardwareInterfaces.get(name).loop(getRuntime());
        }
        resetStartTime();
    }
}
