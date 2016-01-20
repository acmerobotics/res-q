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

    public static final double SERVO_UP = 1;
    public static final double SERVO_DOWN = 0;

    private DcMotor motor;
    private Servo bucket;
    private int target, offset;
    private ArmMode armMode;
    private double lastError = Double.POSITIVE_INFINITY;
    private OpMode mode;

    public enum ArmMode {
        NORMAL,
        RUN_TO_POSITION
    }

    @Override
    public void init(OpMode mode) {
        this.mode = mode;
        motor = mode.hardwareMap.dcMotor.get("step");
        motor.setDirection(DcMotor.Direction.REVERSE);
        offset = motor.getCurrentPosition();
        this.armMode = ArmMode.NORMAL;
        bucket = mode.hardwareMap.servo.get("bucket");
        setBucketPosition(SERVO_UP);
    }

    @Override
    public void loop(double timeSinceLastLoop) {
        mode.telemetry.addData("Arm Position", this.getPosition());
        mode.telemetry.addData("Error", lastError);
        mode.telemetry.addData("Target", target);
        if (armMode == ArmMode.RUN_TO_POSITION) {
            double error = target - getPosition();
            lastError = error;
            motor.setPower(Range.clip(error * 0.0025, -0.25, 0.25));
        }
    }

    public ArmMode getArmMode() {
        return armMode;
    }

    public void moveToFront() {
        this.setArmPosition(13800);
    }

    public void moveToBack() {
        this.setArmPosition(0);
    }

    public double getPosition() {
        return motor.getCurrentPosition() - offset;
    }

    public boolean isBusy() {
        return Math.abs(lastError) > 10;
    }

    public void setArmMode(ArmMode newMode) {
        this.armMode = newMode;
    }

    public void setArmPosition(int target) {
        setArmMode(ArmMode.RUN_TO_POSITION);
        this.target = target;
    }

    public void setArmPower(double val) {
        setArmMode(ArmMode.NORMAL);
        motor.setPower(Range.clip(val, -1, 1));
    }

    public void setBucketPosition(double i) {
        bucket.setPosition(i);
    }
}
