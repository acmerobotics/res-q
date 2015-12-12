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
    private int target, offset;
    private ArmMode mode;
    private double lastError = 0.0;

    public enum ArmMode {
        NORMAL,
        RUN_TO_POSITION
    }

    @Override
    public void init(OpMode mode) {
        motor = mode.hardwareMap.dcMotor.get("step");
        offset = motor.getCurrentPosition();
        this.mode = ArmMode.NORMAL;
    }

    @Override
    public void loop(double timeSinceLastLoop) {
        if (mode == ArmMode.RUN_TO_POSITION) {
            double error = target - motor.getCurrentPosition();
            motor.setPower(Range.clip(error * 0.0025, -1, 1));
        }
    }

    public boolean isBusy() {
        return Math.abs(lastError) > 5;
    }

    public void setMode(ArmMode newMode) {
        this.mode = newMode;
    }

    public void setArmPosition(int target) {
        if (!mode.equals(ArmMode.RUN_TO_POSITION)) return;
        this.target = target + offset;
    }

    public void setArmPower(double val) {
        if (!mode.equals(ArmMode.NORMAL)) return;
        motor.setPower(Range.clip(val, -0.5, 0.5));
    }

    public void setBucketPosition() {

    }

    public void dump() {

    }
}
