package com.qualcomm.ftcrobotcontroller.hardware.mechanisms;

import com.qualcomm.ftcrobotcontroller.hardware.HardwareInterface;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Admin on 12/11/2015.
 */
public class HitterHardware extends HardwareInterface {

    public static double MIN = 0.0, MAX = 0.65;
    private Servo left, right;
    private boolean leftExtended, rightExtended;

    @Override
    public void init(OpMode mode) {
        left = mode.hardwareMap.servo.get("lefthitter");
        right = mode.hardwareMap.servo.get("righthitter");
        retractLeft();
        retractRight();
    }

    public void extendLeft() {
        leftExtended = true;
        left.setPosition(MAX);
    }

    public void retractLeft() {
        leftExtended = false;
        left.setPosition(MIN);
    }

    public void toggleLeft() {
        if (leftExtended) {
            retractLeft();
        } else {
            extendLeft();
        }
    }

    public void extendRight() {
        rightExtended = true;
        right.setPosition(MAX);
    }

    public void retractRight() {
        rightExtended = false;
        right.setPosition(MIN);
    }

    public void toggleRight() {
        if (rightExtended) {
            retractRight();
        } else {
            extendRight();
        }
    }
}
