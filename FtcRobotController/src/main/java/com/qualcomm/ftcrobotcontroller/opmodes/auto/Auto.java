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

/**
 * Created by Ryan on 12/10/2015.
 */
public class Auto extends LinearRobotController {

    public static int LIGHT_THRESHOLD = 200;

    protected DriveHardware driveHardware;
    protected SmartDriveHardware smartDriveHardware;
    protected IMUHardware gyroHardware;
    protected UltrasonicPairHardware usHardware;
    protected PuncherHardware puncherHardware;
    protected ArmHardware armHardware;
    protected FlipperHardware flipperHardware;
    protected ColorSensor lineColorSensor;
    protected ColorSensor frontColorSensor;

    public enum LineColor {
        LIGHT,
        DARK
    }

    public static double cm(double in) {
        return in * 2.54;
    }

    public LineColor getLineColor() {
        return lineColorSensor.alpha() > LIGHT_THRESHOLD ? LineColor.LIGHT : LineColor.DARK;
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

        driveHardware = new DriveHardware();
        gyroHardware = new IMUHardware();
        smartDriveHardware = new SmartDriveHardware(driveHardware, gyroHardware);

        usHardware = new UltrasonicPairHardware();
        puncherHardware = new PuncherHardware();
        armHardware = new ArmHardware();
        flipperHardware = new FlipperHardware();

        registerHardwareInterface("drive", driveHardware);
        registerHardwareInterface("gyro", gyroHardware);
        registerHardwareInterface("gyro_drive", smartDriveHardware);
        registerHardwareInterface("us", usHardware);
        registerHardwareInterface("pusher", puncherHardware);
        registerHardwareInterface("arm", armHardware);
        registerHardwareInterface("flipper", flipperHardware);
    }
}
