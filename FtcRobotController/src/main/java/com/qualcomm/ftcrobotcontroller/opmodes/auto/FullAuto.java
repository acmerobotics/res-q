package com.qualcomm.ftcrobotcontroller.opmodes.auto;

import com.qualcomm.ftcrobotcontroller.hardware.drive.DriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.drive.SmartDriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.ArmHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.FlipperHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.PuncherHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.IMUHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.ColorHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.UltrasonicPairHardware;

/**
 * Created by Ryan on 12/10/2015.
 */
public class FullAuto extends Auto {

    public static double BOT_LENGTH = cm(18.0),
                         LENGTH_FROM_START = cm(32.0),
                         LENGTH_TO_END = 46.0,
                         LENGTH_FROM_STATION = 13.0,
                         THETA = 135,
                         PHI = 180 - THETA;

    private DriveHardware driveHardware;
    private SmartDriveHardware smartDriveHardware;
    private IMUHardware gyroHardware;
    private UltrasonicPairHardware usHardware;
    private ColorHardware colorHardware;
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
        gyroHardware = new IMUHardware();
        smartDriveHardware = new SmartDriveHardware(driveHardware, gyroHardware);

        usHardware = new UltrasonicPairHardware();
        colorHardware = new ColorHardware();
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

        this.alignWithWall();

        do {
            driveHardware.setMotorSpeeds(0.075, 0.075);
            waitOneFullHardwareCycle();
        } while (usHardware.getDistance() > LENGTH_FROM_STATION);
        driveHardware.stopMotors();

        this.pushButtons();

        flipperHardware.dump();
    }
}
