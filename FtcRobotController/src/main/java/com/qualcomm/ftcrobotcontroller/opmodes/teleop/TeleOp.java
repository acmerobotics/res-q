package com.qualcomm.ftcrobotcontroller.opmodes.teleop;

import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.ArmHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.HitterHardware;

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
    }

    @Override
    public void loop() {
        armHardware.setArmPower(0.5 * gamepad2.left_stick_x);
        armHardware.setBucketPosition();

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
