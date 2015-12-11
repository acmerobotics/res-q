package com.qualcomm.ftcrobotcontroller.hardware;

import android.util.Log;

import com.qualcomm.ftcrobotcontroller.util.Helper;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Admin on 9/21/2015.
 */
public class DriveHardware extends HardwareInterface {

    public static final double SENSITIVITY = 1.5;
    public static final double RAMP_TIME = 0.05;
    private DcMotorController leftCtrl, rightCtrl;
    private DcMotor[] leftMotors = new DcMotor[2], rightMotors = new DcMotor[2];
    private HardwareMap hardwareMap;
    private double actualLeft, actualRight, targetLeft, targetRight;
    private double leftRate, rightRate;

    @Override
    public void init(OpMode mode) {
        hardwareMap = mode.hardwareMap;
        leftCtrl = this.hardwareMap.dcMotorController.get("left");
        rightCtrl = this.hardwareMap.dcMotorController.get("right");
        leftMotors[0] = this.hardwareMap.dcMotor.get("left1");
        leftMotors[1] = this.hardwareMap.dcMotor.get("left2");
        rightMotors[0] = this.hardwareMap.dcMotor.get("right1");
        rightMotors[1] = this.hardwareMap.dcMotor.get("right2");
    }

    @Override
    public void loop(double timeSinceLastLoop) {
        if ((leftRate > 0 && actualLeft < targetLeft) || (leftRate < 0 && actualLeft > targetLeft)) {
            actualLeft += leftRate * timeSinceLastLoop;
        }
        if ((rightRate > 0 && actualRight < targetRight) || (rightRate < 0 && actualRight > targetRight)) {
            actualRight += rightRate * timeSinceLastLoop;
        }
        this.leftMotors[0].setPower(actualLeft);
        this.leftMotors[1].setPower(actualLeft);
        this.rightMotors[0].setPower(-actualRight);
        this.rightMotors[1].setPower(-actualRight);
    }

    public void setMotorSpeeds(double leftSpeed, double rightSpeed) {
        leftSpeed = Range.clip(leftSpeed, -1, 1);
        rightSpeed = Range.clip(rightSpeed, -1, 1);
        targetLeft = leftSpeed;
        targetRight = rightSpeed;
        leftRate = (targetLeft - actualLeft) / RAMP_TIME;
        rightRate = (targetRight - actualRight) / RAMP_TIME;
        logcat("Left: " + leftMotors[0].getPower() + "/" + leftMotors[1].getPower() + "\tRight: " + rightMotors[0].getPower() + "/" + rightMotors[1].getPower());
    }

    public double mapMotorSpeed(double speed) {
        double newSpeed = 0.8 * Math.pow(Math.abs(speed), SENSITIVITY);
        return speed > 0 ? newSpeed : -newSpeed;
    }

    public void setMappedMotorSpeeds(double leftSpeed, double rightSpeed) {
        setMotorSpeeds(mapMotorSpeed(leftSpeed), mapMotorSpeed(rightSpeed));
    }

    public void stopMotors() {
        setMotorSpeeds(0, 0);
    }
}