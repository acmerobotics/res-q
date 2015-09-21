package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Admin on 9/21/2015.
 */
public abstract class TreadHardware extends OpMode {
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

    public void setMotorSpeeds(int leftSpeed, int rightSpeed) {
        this.leftCtrl.setMotorPower(leftSpeed);
        
    }
}