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
public class FullAuto extends LinearRobotController {

    public static double BOT_LENGTH = cm(18.0),
                         LENGTH_FROM_START = cm(32.0),
                         LENGTH_TO_END = 46.0,
                         LENGTH_FROM_STATION = 13.0,
                         THETA = 135,
                         PHI = 180 - THETA;

    private DriveHardware driveHardware;
    private SmartDriveHardware smartDriveHardware;
    private I2cIMUHardware gyroHardware;
    private UltrasonicPairHardware usHardware;
    private I2cColorHardware colorHardware;
    private PuncherHardware puncherHardware;
    private ArmHardware armHardware;
    private FlipperHardware flipperHardware;

    public static double cm(double in) {
        return in * 2.54;
    }

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

        while (usHardware.getDistance() > LENGTH_TO_END) {
            driveHardware.setMotorSpeeds(0.25, 0.25);
            waitOneFullHardwareCycle();
        }
        driveHardware.stopMotors();

        if (getAllianceColor().equals(AllianceColor.BLUE)) {
            smartDriveHardware.turnRightSync(PHI);
        } else {
            smartDriveHardware.turnLeftSync(PHI);
        }

        // experimental
        double diff, speed;
        do {
            diff = usHardware.getDifference();
            speed = diff * -0.1;
            driveHardware.setMotorSpeeds(-speed, speed);
            waitOneFullHardwareCycle();
        } while (Math.abs(diff) > 1.0);
        // end experimental

        do {
            driveHardware.setMotorSpeeds(0.075, 0.075);
            waitOneFullHardwareCycle();
        } while (usHardware.getDistance() > LENGTH_FROM_STATION);
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
