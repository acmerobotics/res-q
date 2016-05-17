package com.qualcomm.ftcrobotcontroller.opmodes.teleop;

import com.qualcomm.ftcrobotcontroller.control.RobotController;
import com.qualcomm.ftcrobotcontroller.hardware.drive.DriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.ArmHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.FlipperHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.PuncherHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.WinchHardware;

/**
 * Created by Admin on 12/11/2015.
 */
public class TeleOp extends RobotController {

    public enum OperationMode {
        NORMAL,
        DEMO
    }

    public enum DriveMode {
        ARCADE,
        TANK
    }

    protected ArmHardware armHardware;
    protected FlipperHardware flipperHardware;
    protected WinchHardware winchHardware;
    protected DriveHardware driveHardware;

    protected ArcadeDrive arcadeDrive;
    protected TankDrive tankDrive;

    protected OperationMode operationMode;
    protected DriveMode driveMode;

    protected double servoPosition = ArmHardware.SERVO_UP;

    protected boolean rightBumper = false;

    @Override
    public void init() {
        super.init();

        armHardware = new ArmHardware();
        flipperHardware = new FlipperHardware();
        winchHardware = new WinchHardware();
        driveHardware = new DriveHardware();

        arcadeDrive = new ArcadeDrive();
        tankDrive = new TankDrive();

        setOperationMode(OperationMode.NORMAL);
        setDriveMode(DriveMode.ARCADE);

        registerHardwareInterface("arm", armHardware);
        registerHardwareInterface("flipper", flipperHardware);
        registerHardwareInterface("puncher", new PuncherHardware()); // this resets the puncher position
        registerHardwareInterface("winch", winchHardware);
        registerHardwareInterface("drive", driveHardware);
    }

    public void setOperationMode(OperationMode operationMode) {
        this.operationMode = operationMode;
    }

    public void setDriveMode(DriveMode mode) {
        this.driveMode = mode;
    }

    public OperationMode getOperationMode() {
        return operationMode;
    }

    public DriveMode getDriveMode() {
        return driveMode;
    }

    @Override
    public void loop() {
        // drive system
        switch (getDriveMode()) {
            case ARCADE:
                arcadeDrive.loop(driveHardware, gamepad1);
                break;
            case TANK:
                tankDrive.loop(driveHardware, gamepad1);
                break;
        }

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
            servoPosition += servoThrottle * 0.0075;
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

        if (!getOperationMode().equals(OperationMode.DEMO)) {
            // winch
            if (gamepad1.y) {
                winchHardware.beginExtending();
            } else if (gamepad1.a && !gamepad1.start) {
                winchHardware.beginRetracting();
            } else {
                winchHardware.stop();
            }
        }

        super.loop();
    }
}
