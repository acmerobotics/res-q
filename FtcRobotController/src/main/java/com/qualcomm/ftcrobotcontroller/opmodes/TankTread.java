package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Admin on 9/21/2015.
 */
public class TankTread extends TreadHardware {
    @Override
    public void loop() {
        setMotorSpeeds(gamepad1.left_stick_y * 0.8, gamepad1.right_stick_y * 0.8);
    }
}
