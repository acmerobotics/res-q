package com.qualcomm.ftcrobotcontroller.opmodes.auto;

/**
 * Created by Admin on 1/29/2016.
 */
public class DumbAuto extends Auto {

    @Override
    public void runOpMode() throws InterruptedException {
            super.runOpMode();


        promptAllianceColor();

        waitForStart();

        do {
            smartDriveHardware.driveStraight();
            waitOneFullHardwareCycle();
        } while (getLineColor().equals(LineColor.DARK));
        //waitMillis(200);
        smartDriveHardware.stopMotors();

        alignWithWall();

        do {
            driveHardware.setMotorSpeeds(0.1, 0.1);
            waitOneFullHardwareCycle();
        } while (usHardware.getDistance() > 15);
        driveHardware.stopMotors();

        flipperHardware.dump();
    }
}
