package com.qualcomm.ftcrobotcontroller.hardware.mechanisms;

import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.ftcrobotcontroller.hardware.HardwareInterface;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Ryan on 12/11/2015.
 */
public class PuncherHardware extends HardwareInterface {

    private Servo p1, p2;
    private OpMode controller;

    @Override
    public void init(OpMode mode) {
        controller = mode;
        p1 = mode.hardwareMap.servo.get("leftpuncher");
        p2 = mode.hardwareMap.servo.get("rightpuncher");
        p1.setDirection(Servo.Direction.REVERSE);
        p2.setDirection(Servo.Direction.REVERSE);
        p1.setPosition(0);
        p2.setPosition(1);
    }

    @Override
    public void loop(double timeSinceLastLoop) {
        super.loop(timeSinceLastLoop);
    }

    @Override
    public String getStatusString() {
        return "left: " + p1.getPosition() + "  right: " + p2.getPosition();
    }

    public void punchLeft() {
        p1.setPosition(1);
        ((LinearRobotController) controller).waitMillis(1500);
        p1.setPosition(0);
    }

    public void punchRight() {
        p2.setPosition(1);
        ((LinearRobotController) controller).waitMillis(1500);
        p2.setPosition(0);
    }
}
