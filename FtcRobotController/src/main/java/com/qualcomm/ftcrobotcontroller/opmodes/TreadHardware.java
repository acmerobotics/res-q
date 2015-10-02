package com.qualcomm.ftcrobotcontroller.opmodes;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Admin on 9/21/2015.
 */
public abstract class TreadHardware extends OpMode {
    public static final double SENSITIVITY = 1.5;
    private DcMotorController leftCtrl, rightCtrl;
    private DcMotor[] leftMotors = new DcMotor[2], rightMotors = new DcMotor[2];
    @Override
    public void init() {
        leftCtrl = this.hardwareMap.dcMotorController.get("left");
        rightCtrl = this.hardwareMap.dcMotorController.get("right");
        leftMotors[0] = this.hardwareMap.dcMotor.get("left1");
        leftMotors[1] = this.hardwareMap.dcMotor.get("left2");
        rightMotors[0] = this.hardwareMap.dcMotor.get("right1");
        rightMotors[1] = this.hardwareMap.dcMotor.get("right2");
    }

    public void setMotorSpeeds(double leftSpeed, double rightSpeed) {
        this.leftMotors[0].setPower(-leftSpeed);
        this.leftMotors[1].setPower(-leftSpeed);
        this.rightMotors[0].setPower(rightSpeed);
        this.rightMotors[1].setPower(rightSpeed);
        Log.i("Speeds", leftMotors[0].getPower() + "," + leftMotors[1].getPower() + "," + rightMotors[0].getPower() + "," + rightMotors[1].getPower());
    }

    public double mapMotorSpeed(double speed) {
        double newSpeed = 0.8 * Math.pow(Math.abs(speed), SENSITIVITY);
        return speed > 0 ? newSpeed : -newSpeed;
    }

    public void setMappedMotorSpeeds(double leftSpeed, double rightSpeed) {
        setMotorSpeeds(mapMotorSpeed(leftSpeed), mapMotorSpeed(rightSpeed));
    }
}