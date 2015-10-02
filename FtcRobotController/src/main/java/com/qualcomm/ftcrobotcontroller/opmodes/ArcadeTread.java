package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Admin on 9/24/2015.
 */
public class ArcadeTread extends TreadHardware {
    @Override
    public void loop() {
        double leftRight = gamepad1.right_stick_x;
        double forwardBack = gamepad1.left_stick_y;
        if (Math.abs(leftRight) > 0.05) {
            setMappedMotorSpeeds(leftRight, -leftRight);
        } else if (Math.abs(forwardBack) > 0.05) {
            setMappedMotorSpeeds(forwardBack, forwardBack);
        } else {
            setMotorSpeeds(0, 0);
        }
    }

}
