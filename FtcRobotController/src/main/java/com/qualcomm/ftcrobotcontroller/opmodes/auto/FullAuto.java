package com.qualcomm.ftcrobotcontroller.opmodes.auto;

import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.ftcrobotcontroller.hardware.drive.DriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.drive.GyroDriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.ArmHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.PusherHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.I2cGyroHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.I2cColorHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.UltrasonicPairHardware;

/**
 * Created by Ryan on 12/10/2015.
 */
public class FullAuto extends LinearRobotController {

    public static double LENGTH1 = 50.0, LENGTH2 = 20.0, LENGTH3 = 4.0;

    private DriveHardware driveHardware;
    private GyroDriveHardware gyroDriveHardware;
    private I2cGyroHardware gyroHardware;
    private UltrasonicPairHardware usHardware;
    private I2cColorHardware colorHardware;
    private PusherHardware pusherHardware;
    private ArmHardware armHardware;

    @Override
    public void runOpMode() throws InterruptedException {
        driveHardware = new DriveHardware();
        gyroHardware = new I2cGyroHardware();
        gyroDriveHardware = new GyroDriveHardware(driveHardware, gyroHardware);
        usHardware = new UltrasonicPairHardware();
        colorHardware = new I2cColorHardware();
        pusherHardware = new PusherHardware();
        armHardware = new ArmHardware();

        registerHardwareInterface("drive", driveHardware);
        registerHardwareInterface("gyro", gyroHardware);
        registerHardwareInterface("gyro_drive", gyroDriveHardware);
        registerHardwareInterface("us", usHardware);
        registerHardwareInterface("color", colorHardware);
        registerHardwareInterface("pusher", pusherHardware);
        registerHardwareInterface("arm", armHardware);

        promptAllianceColor();

        waitForStart();

        driveHardware.setMotorSpeeds(-0.5, -0.5);
        while (usHardware.getDistance() < LENGTH1) {
            waitOneFullHardwareCycle();
        }
        driveHardware.stopMotors();

        gyroDriveHardware.turnLeftSync(135.0);

        driveHardware.setMotorSpeeds(0.5, 0.5);
        while (usHardware.getDistance() >  LENGTH2) {
            waitOneFullHardwareCycle();
        }
        driveHardware.stopMotors();

        gyroDriveHardware.turnRightSync(45.0);

        driveHardware.setMotorSpeeds(0.2, 0.2);
        while (usHardware.getDistance() < 4) {
            waitOneFullHardwareCycle();
        }
        driveHardware.stopMotors();

        I2cColorHardware.Color color;
        do {
            color = colorHardware.getPredominantColor();
        } while (color != I2cColorHardware.Color.BLUE && color != I2cColorHardware.Color.RED);

        if (color.toString() == getAllianceColor().toString()) {
            // right side
            pusherHardware.pushRight();
        } else {
            // left side
            pusherHardware.pushLeft();
        }

        armHardware.dump();
    }
}
