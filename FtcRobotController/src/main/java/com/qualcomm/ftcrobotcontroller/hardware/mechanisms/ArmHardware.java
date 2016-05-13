package com.qualcomm.ftcrobotcontroller.hardware.mechanisms;

import com.qualcomm.ftcrobotcontroller.hardware.HardwareInterface;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Ryan on 12/11/2015.
 */
public class ArmHardware extends HardwareInterface {

    public static final double SERVO_UP = 1;
    public static final double SERVO_DOWN = 0;
    public static final int ENCODER_MAX = 13500;
    public static final int ENCODER_MIN = 0;

    private boolean constrained;
    private boolean calibrated;

    private DcMotor motor;
    private Servo bucket;
    private TouchSensor touch;
    private int target, offset;
    private ArmMode armMode;
    private double lastError = Double.POSITIVE_INFINITY;
    private OpMode mode;

    public enum ArmMode {
        NORMAL,
        CALIBRATING,
        RUN_TO_POSITION
    }

    public ArmHardware() {
        this(true);
    }

    public ArmHardware(boolean r) {
        constrained = r;
        calibrated = false;
    }

    @Override
    public void init(OpMode mode) {
        this.mode = mode;

        motor = mode.hardwareMap.dcMotor.get("step");
        bucket = mode.hardwareMap.servo.get("bucket");
        touch = mode.hardwareMap.touchSensor.get("touch");

        // motor.setDirection(DcMotor.Direction.REVERSE);
        resetEncoders();
        setArmMode(ArmMode.NORMAL);
        setBucketPosition(SERVO_UP);
        setConstrained(constrained);
        calibrate();
    }

    @Override
    public void loop(double timeSinceLastLoop) {
        boolean buttonState = touch.isPressed();
        if (buttonState) {
            resetEncoders();
        }

        switch (armMode) {
            case RUN_TO_POSITION:
                double error = target - getPosition();
                lastError = error;
                motor.setPower(Range.clip(error * 0.0025, -0.5, 0.5));
                break;
            case CALIBRATING:
                if (buttonState) {
                    setArmMode(ArmMode.NORMAL);
                    calibrated = true;
                } else {
                    motor.setPower(-0.1);
                }
        }

        if (isConstrained() && (getPosition() > ENCODER_MAX || getPosition() < ENCODER_MIN)) {
            motor.setPower(0);
        }
    }

    public void calibrate() {
        if (isConstrained()) setArmMode(ArmMode.CALIBRATING);
    }

    public boolean isCalibrated() {
        return !isConstrained() || calibrated;
    }

    @Override
    public String getStatusString() {
        return "constrained: " + constrained + "  position: " + this.getPosition() + "  error: " + Double.toString(lastError).toLowerCase() + "  target: " + target + "  servo: " + bucket.getPosition() + "  calibrated: " + isCalibrated();
    }

    public boolean isConstrained() {
        return constrained;
    }

    public void setConstrained(boolean a) {
        constrained = a;
    }

    public ArmMode getArmMode() {
        return armMode;
    }

    public void moveToFront() {
        this.setArmPosition(ENCODER_MAX);
    }

    public void moveToBack() {
        this.setArmPosition(ENCODER_MIN);
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
        if (target > ENCODER_MAX) {
            target = ENCODER_MAX;
        } else if (target < ENCODER_MIN) {
            target = ENCODER_MIN;
        }
        this.target = target;
    }

    public void setArmPower(double val) {
        setArmMode(ArmMode.NORMAL);
        motor.setPower(Range.clip(val, -1, 1));
    }

    public void resetEncoders() {
        offset = motor.getCurrentPosition();
    }

    public void setBucketPosition(double i) {
        bucket.setPosition(i);
    }
}
