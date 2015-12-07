package com.qualcomm.ftcrobotcontroller.hardware;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * Created by Ryan on 12/6/2015.
 */
public class LinearRobotController extends LinearOpMode {

    public RobotController robotController;

    public LinearRobotController() {
        super();
        robotController = new RobotController();
        robotController.init();
    }

    @Override
    public void runOpMode() throws InterruptedException {

    }

    @Override
    public void waitOneFullHardwareCycle() throws InterruptedException {
        robotController.loop();
        super.waitOneFullHardwareCycle();
    }

    public void registerHardwareInterface(String name, HardwareInterface hi) {
        robotController.registerHardwareInterface(name, hi);
    }

    public void deregisterHardwareInterface(String name) {
        robotController.deregisterHardwareInterface(name);
    }

    public void waitMillis(long ms) {
        long targetTime = System.currentTimeMillis() + ms;
        while (System.currentTimeMillis() < targetTime) {
            try {
                waitOneFullHardwareCycle();
            } catch (InterruptedException e) {
                RobotLog.d(e.getMessage());
            }
        }
    }
}
