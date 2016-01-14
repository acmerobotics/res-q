package com.qualcomm.ftcrobotcontroller.control;

import com.qualcomm.ftcrobotcontroller.hardware.HardwareInterface;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * Created by Ryan on 12/6/2015.
 */
public class LinearRobotController extends LinearOpMode {

    public RobotController robotController;

    private AllianceColor color = AllianceColor.UNSET;

    public enum AllianceColor {
        BLUE,
        RED,
        UNSET
    }

    public LinearRobotController() {
        robotController = new RobotController(this);
    }

    @Override
    public void waitForStart() throws InterruptedException {
        super.waitForStart();
        this.loop();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        robotController.init();
    }

    @Override
    public void waitOneFullHardwareCycle() throws InterruptedException {
        if (isAllianceColorSet()) telemetry.addData("Alliance Color", getAllianceColor().toString());
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
        while (true) {
            telemetry.addData("Alliance Color", "Press [X] for blue or [B] for red");
            if (gamepad1.x) {
                // blue
                this.color = AllianceColor.BLUE;
                break;
            } else if (gamepad1.b) {
                // red
                this.color = AllianceColor.RED;
                break;
            }
            try {
                waitOneFullHardwareCycle();
            } catch (InterruptedException e) {
                RobotLog.e(e.getMessage());
            }
        }
        while (!opModeIsActive()) {
            try {
                waitOneFullHardwareCycle();
            } catch (InterruptedException e) {
                RobotLog.e(e.getMessage());
            }
        }
        telemetry.clearData();
    }

    @Override
    public double getRuntime() {
        return robotController.getRuntime();
    }

    public AllianceColor getAllianceColor() {
        return color;
    }

    public boolean isAllianceColorSet() {
        return !color.equals(AllianceColor.UNSET);
    }
}
