package com.qualcomm.ftcrobotcontroller.opmodes.teleop;

import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.ArmHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.FlipperHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.PuncherHardware;

/**
 * Created by Admin on 12/11/2015.
 */
public class TeleOp extends ArcadeDrive {

    protected ArmHardware armHardware;
    protected FlipperHardware flipperHardware;

    protected double servoPosition = ArmHardware.SERVO_UP;

    protected boolean rightBumper = false;

    @Override
    public void init() {
        super.init();

        armHardware = new ArmHardware();
        flipperHardware = new FlipperHardware();

        registerHardwareInterface("arm", armHardware);
        registerHardwareInterface("flipper", flipperHardware);
        registerHardwareInterface("puncher", new PuncherHardware());
    }

    @Override
    public void loop() {
        // deploy flipper
        if (gamepad1.right_bumper) {
            if (!rightBumper) {
                flipperHardware.toggle();
            }
            rightBumper = true;
        } else {
            rightBumper = false;
        }

        // arm joystick
        double armThrottle = -gamepad2.left_stick_y;
        if (Math.abs(armThrottle) > 0.05) {
            armHardware.setArmPower(armThrottle * 0.75);
        } else if (armHardware.getArmMode() == ArmHardware.ArmMode.NORMAL) {
            armHardware.setArmPower(0);
        }

        // arm buttons
        if (gamepad2.dpad_up) {
            armHardware.moveToFront();
        }
        if (gamepad2.dpad_down) {
            armHardware.moveToBack();
        }

        // servo joystick
        double servoThrottle = gamepad2.right_stick_y;
        if (Math.abs(servoThrottle) > 0.05) {
            servoPosition += servoThrottle * 0.015;
        }
        if (servoPosition > ArmHardware.SERVO_UP) {
            servoPosition = ArmHardware.SERVO_UP;
        } else if (servoPosition < ArmHardware.SERVO_DOWN) {
            servoPosition = ArmHardware.SERVO_DOWN;
        }

        // servo buttons
        if (gamepad2.x) {
            servoPosition = ArmHardware.SERVO_UP;
        }
        if (gamepad2.a) {
            servoPosition = ArmHardware.SERVO_DOWN;
        }

        // actually set the servos
        armHardware.setBucketPosition(servoPosition);

        // reset encoders
        if (gamepad2.right_bumper && gamepad2.left_bumper) {
            armHardware.resetEncoders();
        }

        super.loop();
    }
}
