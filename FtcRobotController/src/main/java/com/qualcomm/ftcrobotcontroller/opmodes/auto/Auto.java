package com.qualcomm.ftcrobotcontroller.opmodes.auto;

import android.graphics.Color;

import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.ftcrobotcontroller.hardware.drive.DriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.drive.SmartDriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.ArmHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.FlipperHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.PuncherHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.IMUHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.ColorHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.UltrasonicPairHardware;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by Ryan on 12/10/2015.
 */
public class Auto extends LinearRobotController {

    protected DriveHardware driveHardware;
    protected SmartDriveHardware smartDriveHardware;
    protected GyroSensor gyroSensor;
    protected UltrasonicPairHardware usHardware;
    protected PuncherHardware puncherHardware;
    protected ArmHardware armHardware;
    protected FlipperHardware flipperHardware;
    protected ColorSensor lineColorSensor;
    protected ColorSensor frontColorSensor;

    public enum LineColor {
        DARK,
        LIGHT
    }


    /**
     * @param a number
     * @return -1 or 1 depending on the sign of a
     */
    public double sign(double a) {
        return a > 0 ? 1 : -1;
    }

    /** 0 = DARK, 1-5 = LIGHT (greater = stronger) */
    public double getLineStrength() {
        return lineColorSensor.alpha();
    }

    public LineColor getLineColor() {
        return getLineStrength() > 1 ? LineColor.LIGHT : LineColor.DARK;
    }

    public boolean isFrontRed() {
        int r = lineColorSensor.red(),
            b = lineColorSensor.blue();
        return r > b;
    }

    protected void alignWithWall() throws InterruptedException {
        // experimental
        double diff, speed;
        do {
            diff = usHardware.getDifference();
            speed = diff * 0.05;
            driveHardware.setMotorSpeeds(speed, -speed);
            waitOneFullHardwareCycle();
        } while (Math.abs(diff) > 1.5);
        driveHardware.stopMotors();
        // end experimental
    }

    protected void pushButtons() {
        if (isFrontRed() && getAllianceColor() == AllianceColor.RED) {
            // right side
            puncherHardware.punchRight();
        } else {
            // left side
            puncherHardware.punchLeft();
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        frontColorSensor = hardwareMap.colorSensor.get("front");
        lineColorSensor = hardwareMap.colorSensor.get("line");

        frontColorSensor.enableLed(false);

        lineColorSensor.setI2cAddress(0x3e);

        driveHardware = new DriveHardware();
        gyroSensor = hardwareMap.gyroSensor.get("gyro");
        smartDriveHardware = new SmartDriveHardware(driveHardware, gyroSensor);

        usHardware = new UltrasonicPairHardware();
        puncherHardware = new PuncherHardware();
        armHardware = new ArmHardware();
        flipperHardware = new FlipperHardware();

        registerHardwareInterface("drive", driveHardware);
        registerHardwareInterface("gyro_drive", smartDriveHardware);
        registerHardwareInterface("us", usHardware);
        registerHardwareInterface("pusher", puncherHardware);
        registerHardwareInterface("arm", armHardware);
        registerHardwareInterface("flipper", flipperHardware);
    }
}
