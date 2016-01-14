package com.qualcomm.ftcrobotcontroller.hardware.mechanisms;

import com.qualcomm.ftcrobotcontroller.hardware.HardwareInterface;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Ryan on 12/11/2015.
 */
public class ArmHardware extends HardwareInterface {

    private DcMotor motor;
    private Servo bucket;
    private int target, offset;
    private ArmMode armMode;
    private BucketMode bucketMode;
    private double lastError = 0.0;

    public enum ArmMode {
        NORMAL,
        RUN_TO_POSITION
    }

    public enum BucketMode {
        FORWARD,
        REVERSE,
        STOPPED
    }

    @Override
    public void init(OpMode mode) {
        motor = mode.hardwareMap.dcMotor.get("step");
        offset = motor.getCurrentPosition();
        this.armMode = ArmMode.NORMAL;
        bucket = mode.hardwareMap.servo.get("bucket");
        setBucketMode(BucketMode.STOPPED);
    }

    @Override
    public void loop(double timeSinceLastLoop) {
        if (armMode == ArmMode.RUN_TO_POSITION) {
            double error = target - getPosition();
            motor.setPower(Range.clip(error * 0.0025, -1, 1));
        }
    }

    public double getPosition() {
        return motor.getCurrentPosition() - offset;
    }

    public boolean isBusy() {
        return Math.abs(lastError) > 5;
    }

    public void setArmMode(ArmMode newMode) {
        this.armMode = newMode;
    }

    public void setArmPosition(int target) {
        if (!armMode.equals(ArmMode.RUN_TO_POSITION)) return;
        this.target = target;
    }

    public void setArmPower(double val) {
        if (!armMode.equals(ArmMode.NORMAL)) return;
        motor.setPower(Range.clip(val, -1, 1));
    }

    public void setBucketMode(BucketMode mode) {
        bucketMode = mode;
        switch(mode) {
            case STOPPED:
                bucket.setPosition(0.5);
                break;
            case FORWARD:
                bucket.setPosition(1);
                break;
            case REVERSE:
                bucket.setPosition(0);
                break;
        }
    }
}
