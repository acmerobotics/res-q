package com.qualcomm.ftcrobotcontroller.hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Ryan on 12/9/2015.
 */
public class DcMotorStepHardware extends HardwareInterface {

    public static final double K_P = 0.0, K_I = 0.0, K_D = 0.0;

    private DcMotor motor;
    private int targetPosition;

    private double integral, prev;

    public DcMotorStepHardware(DcMotor motor) {
        this.motor = motor;
        integral = 0.0;
        prev = 0.0;
    }

    @Override
    public void init(OpMode mode) {
        targetPosition = motor.getCurrentPosition();
    }

    @Override
    public void loop(double timeSinceLastLoop) {
        super.loop(timeSinceLastLoop);
        // PID
        double error = targetPosition = motor.getCurrentPosition();
        integral += error * timeSinceLastLoop;
        double derivative = (error - prev) / timeSinceLastLoop;
        motor.setPower(error * K_P + integral * K_I + derivative * K_D);
    }

    public void setPosition(int position) {
        targetPosition = position;
    }

    public int getCurrentPosition() {
        return motor.getCurrentPosition();
    }
}