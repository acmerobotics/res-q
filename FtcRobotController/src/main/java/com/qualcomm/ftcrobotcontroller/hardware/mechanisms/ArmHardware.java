package com.qualcomm.ftcrobotcontroller.hardware.mechanisms;

import com.qualcomm.ftcrobotcontroller.hardware.HardwareInterface;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Ryan on 12/11/2015.
 */
public class ArmHardware extends HardwareInterface {

    private DcMotor motor;

    @Override
    public void init(OpMode mode) {
        motor = mode.hardwareMap.dcMotor.get("step");
    }

    public void setArmPower(double val) {
        motor.setPower(Range.clip(val, -0.5, 0.5));
    }

    public void setBucketPosition() {

    }

    public void dump() {

    }
}
