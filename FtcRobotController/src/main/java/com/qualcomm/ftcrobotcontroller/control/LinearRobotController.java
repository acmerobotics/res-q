package com.qualcomm.ftcrobotcontroller.control;

import com.qualcomm.ftcrobotcontroller.hardware.HardwareInterface;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * Created by Ryan on 12/6/2015.
 */
public class LinearRobotController extends LinearOpMode {

    public RobotController robotController;

    public LinearRobotController() {
        robotController = new RobotController(this);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        robotController.init();
    }

    @Override
    public void waitOneFullHardwareCycle() throws InterruptedException {
        robotController.loop();
        super.waitOneFullHardwareCycle();
    }

    public boolean registerHardwareInterface(String name, HardwareInterface hi) {
        return robotController.registerHardwareInterface(name, hi);
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

    public void promptAllianceColor() {
        robotController.promptAllianceColor();
    }

    public boolean isAllianceColorSet() {
        return robotController.isAllianceColorSet();
    }

    public RobotController.AllianceColor getAllianceColor() {
        return robotController.getAllianceColor();
    }
}
