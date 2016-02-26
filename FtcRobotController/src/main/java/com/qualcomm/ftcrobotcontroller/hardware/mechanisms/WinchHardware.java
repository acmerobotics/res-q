package com.qualcomm.ftcrobotcontroller.hardware.mechanisms;

import com.qualcomm.ftcrobotcontroller.hardware.HardwareInterface;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Admin on 2/25/2016.
 */
public class WinchHardware extends HardwareInterface {

    public static final double SPEED = 0.8;

    private DcMotor one, two;
    private WinchState state;

    public enum WinchState {
        EXTENDING,
        RETRACTING,
        NOT_MOVING
    }

    @Override
    public void init(OpMode mode) {
        one = mode.hardwareMap.dcMotor.get("winch1");
        two = mode.hardwareMap.dcMotor.get("winch2");

        state = WinchState.NOT_MOVING;
    }

    @Override
    public String getStatusString() {
        return "state: " + this.state.toString();
    }

    public void beginExtending() {
        state = WinchState.EXTENDING;

        one.setPower(-SPEED);
        two.setPower(-SPEED);
    }

    public void beginRetracting() {
        state = WinchState.RETRACTING;

        one.setPower(SPEED);
        two.setPower(SPEED);
    }

    public void stop() {
        state = WinchState.NOT_MOVING;

        one.setPower(0);
        two.setPower(0);
    }
}
