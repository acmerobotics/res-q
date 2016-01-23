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
    public static final int MOTOR_MAX = 13500;
    public static final int MOTOR_MIN = 0;

    private boolean constrained;

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

    public void ArmHardware() {
        this.constrained = true;
    }

    public void ArmHardware(boolean r) {
        this.constrained = r;
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
        setConstrained(true);
    }

    @Override
    public void loop(double timeSinceLastLoop) {
        if (armMode == ArmMode.RUN_TO_POSITION) {
            double error = target - getPosition();
            lastError = error;
            motor.setPower(Range.clip(error * 0.0025, -0.5, 0.5));
        }
        if (constrained) {
            if (getPosition() > MOTOR_MAX) {
                motor.setPower(0);
            } else if (getPosition() < MOTOR_MIN) {
                motor.setPower(0);
            }
        }
    }

    @Override
    public String getStatusString() {
        return "constrained: " + constrained + "  position: " + this.getPosition() + "  error: " + Double.toString(lastError).toLowerCase() + "  target: " + target + "  servo: " + bucket.getPosition();
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
        this.setArmPosition(MOTOR_MAX);
    }

    public void moveToBack() {
        this.setArmPosition(MOTOR_MIN);
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
        if (target > MOTOR_MAX) {
            target = MOTOR_MAX;
        } else if (target < MOTOR_MIN) {
            target = MOTOR_MIN;
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
