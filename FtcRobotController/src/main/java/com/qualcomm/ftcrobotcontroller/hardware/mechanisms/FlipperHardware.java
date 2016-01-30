package com.qualcomm.ftcrobotcontroller.hardware.mechanisms;

import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.ftcrobotcontroller.hardware.HardwareInterface;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Admin on 1/19/2016.
 */
public class FlipperHardware extends HardwareInterface {

    private Servo servo;
    private OpMode opMode;
    private boolean toggled;

    @Override
    public void init(OpMode mode) {
        opMode = mode;
        servo = mode.hardwareMap.servo.get("flipper");
        this.retract();
    }

    @Override
    public String getStatusString() {
        return "toggled: " + toggled;
    }

    public void dump() {
        this.extend();
        ((LinearRobotController) opMode).waitMillis(2500);
        this.retract();
    }

    public void toggle() {
        if (this.toggled) {
            this.retract();
        } else {
            this.extend();
        }
    }

    public void extend() {
        servo.setPosition(1.0);
        toggled = true;
    }

    public void retract() {
        servo.setPosition(0.0);
        toggled = false;
    }
}
