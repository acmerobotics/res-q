package com.qualcomm.ftcrobotcontroller.control;

import com.qualcomm.ftcrobotcontroller.hardware.HardwareInterface;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.HashMap;

/**
 * Created by Admin on 10/5/2015.
 */


public class RobotController extends OpMode {
    private HashMap<String, HardwareInterface> hardwareInterfaces;
    private OpMode opMode;

    public RobotController() {
        opMode = this;
    }

    public RobotController(OpMode mode) {
        opMode = mode;
    }

    public void init() {
        hardwareInterfaces = new HashMap<String, HardwareInterface>();
    }

    public boolean registerHardwareInterface(String name, HardwareInterface hi) {
        if (hardwareInterfaces.containsKey(name)) {
            return false;
        } else {
            hardwareInterfaces.put(name, hi);
            hi.init(opMode);
            return true;
        }
    }

    public boolean deregisterHardwareInterface(String name) {
        if (hardwareInterfaces.containsKey(name)) {
            hardwareInterfaces.remove(name);
            return true;
        } else {
            return false;
        }
    }

    public void loop() {
        for (String name : hardwareInterfaces.keySet()) {
            HardwareInterface hi = hardwareInterfaces.get(name);
            hi.loop(getRuntime());
            opMode.telemetry.addData(name, hi.getStatusString());
        }
        resetStartTime();
    }
}
