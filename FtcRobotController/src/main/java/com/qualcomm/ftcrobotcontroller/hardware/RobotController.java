package com.qualcomm.ftcrobotcontroller.hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.HashMap;

/**
 * Created by Admin on 10/5/2015.
 */
public class RobotController extends OpMode {
    private HashMap<String, HardwareInterface> hardwareInterfaces;
    private long msLastLoop;
    @Override
    public void init() {
        hardwareInterfaces = new HashMap<String, HardwareInterface>();
        msLastLoop = System.currentTimeMillis();
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

    @Override
    public void loop() {
        for (String name : hardwareInterfaces.keySet()) {
            hardwareInterfaces.get(name).loop(System.currentTimeMillis() - msLastLoop);
        }
        msLastLoop = System.currentTimeMillis();
    }
}
