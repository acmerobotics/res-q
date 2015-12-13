package com.qualcomm.ftcrobotcontroller.opmodes.teleop;

import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.ArmHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.HitterHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.PuncherHardware;

/**
 * Created by Admin on 12/11/2015.
 */
public class TeleOp extends ArcadeDrive {

    private ArmHardware armHardware;
    private HitterHardware hitterHardware;

    private boolean leftBumper = false, rightBumper = false;

    @Override
    public void init() {
        super.init();

        armHardware = new ArmHardware();
        hitterHardware = new HitterHardware();

        registerHardwareInterface("arm", armHardware);
        registerHardwareInterface("hitter", hitterHardware);
        registerHardwareInterface("puncher", new PuncherHardware());
    }

    @Override
    public void loop() {
//        if (gamepad1.dpad_up) {
//            armHardware.setArmPower(0.75);
//        } else if (gamepad1.dpad_down) {
//            armHardware.setArmPower(-0.75);
//        } else {
//            armHardware.setArmPower(0);
//        }
//
        if (gamepad2.x) {
            armHardware.setBucketMode(ArmHardware.BucketMode.REVERSE);
        } else if (gamepad2.b) {
            armHardware.setBucketMode(ArmHardware.BucketMode.FORWARD);
        } else {
            armHardware.setBucketMode(ArmHardware.BucketMode.STOPPED);
        }

        double armThrottle = gamepad2.left_stick_y;

        if (Math.abs(armThrottle) > 0.05) {
            armHardware.setArmPower(armThrottle * 0.75);
        } else {
            armHardware.setArmPower(0);
        }

        if (gamepad1.left_bumper) {
            if (!leftBumper) {
                leftBumper = true;
                hitterHardware.toggleLeft();
            }
        } else {
            leftBumper = false;
        }

        if (gamepad1.right_bumper) {
            if (!rightBumper) {
                rightBumper = true;
                hitterHardware.toggleRight();
            }
        } else {
            rightBumper = false;
        }

        super.loop();
    }
}
