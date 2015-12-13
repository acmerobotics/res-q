package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.control.RobotController;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.ArmHardware;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Ryan on 12/13/2015.
 */
public class BucketTest extends RobotController {

    private ArmHardware armHardware;

    @Override
    public void init() {
        super.init();

        armHardware = new ArmHardware();
        registerHardwareInterface("arm", armHardware);
    }

    @Override
    public void loop() {
        super.loop();

        if (gamepad1.a) {
            //servo.setDirection(Servo.Direction.FORWARD);
            armHardware.setBucketMode(ArmHardware.BucketMode.FORWARD);
        } else if (gamepad1.b) {
            //servo.setDirection(Servo.Direction.REVERSE);
            armHardware.setBucketMode(ArmHardware.BucketMode.REVERSE);
        } else {
            armHardware.setBucketMode(ArmHardware.BucketMode.STOPPED);
        }
    }
}
