package com.qualcomm.ftcrobotcontroller.opmodes.auto;

import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.ftcrobotcontroller.hardware.drive.DriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.drive.SmartDriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.ArmHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.FlipperHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.PuncherHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.I2cIMUHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.I2cColorHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.UltrasonicPairHardware;

/**
 * Created by Ryan on 12/10/2015.
 */
public class TimingAuto extends LinearRobotController {
    private DriveHardware driveHardware;
    private SmartDriveHardware smartDriveHardware;
    private I2cIMUHardware gyroHardware;
    private UltrasonicPairHardware usHardware;
    private I2cColorHardware colorHardware;
    private PuncherHardware puncherHardware;
    private ArmHardware armHardware;
    private FlipperHardware flipperHardware;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        driveHardware = new DriveHardware();
        gyroHardware = new I2cIMUHardware();
        smartDriveHardware = new SmartDriveHardware(driveHardware, gyroHardware);

        usHardware = new UltrasonicPairHardware();
        colorHardware = new I2cColorHardware();
        puncherHardware = new PuncherHardware();
        armHardware = new ArmHardware();
        flipperHardware = new FlipperHardware();

        registerHardwareInterface("drive", driveHardware);
        registerHardwareInterface("gyro", gyroHardware);
        registerHardwareInterface("gyro_drive", smartDriveHardware);
        registerHardwareInterface("us", usHardware);
        registerHardwareInterface("color", colorHardware);
        registerHardwareInterface("pusher", puncherHardware);
        registerHardwareInterface("arm", armHardware);
        registerHardwareInterface("flipper", flipperHardware);

        promptAllianceColor();

        waitForStart();

//        while (usHardware.getDistance() < LENGTH_FROM_START) {
//            driveHardware.setMotorSpeeds(-0.15, -0.15);
//            waitOneFullHardwareCycle();
//        }
//        driveHardware.stopMotors();
//
//        if (getAllianceColor().equals(AllianceColor.BLUE)) {
//            smartDriveHardware.turnLeftSync(THETA);
//        } else {
//            smartDriveHardware.turnRightSync(THETA);
//        }

        driveHardware.setMotorSpeeds(0.3, 0.3);
        waitMillis(4700);
        driveHardware.stopMotors();

        if (getAllianceColor().equals(AllianceColor.BLUE)) {
            smartDriveHardware.turnRightSync(35);
        } else {
            smartDriveHardware.turnLeftSync(45);
        }

        driveHardware.setMotorSpeeds(0.1, 0.1);
        waitMillis(1600);
        driveHardware.stopMotors();

        I2cColorHardware.Color color;
        do {
            color = colorHardware.getPredominantColor();
        } while (color != I2cColorHardware.Color.BLUE && color != I2cColorHardware.Color.RED);

        if (color.toString().equals(getAllianceColor().toString())) {
            // right side
            puncherHardware.punchRight();
        } else {
            // left side
            puncherHardware.punchLeft();
        }

        flipperHardware.dump();
    }
}
