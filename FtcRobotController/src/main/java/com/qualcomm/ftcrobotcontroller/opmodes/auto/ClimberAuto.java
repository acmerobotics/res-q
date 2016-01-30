package com.qualcomm.ftcrobotcontroller.opmodes.auto;

import com.qualcomm.ftcrobotcontroller.hardware.sensors.ODSHardware;

/**
 * Created by Admin on 1/29/2016.
 */
public class ClimberAuto extends Auto {

    protected ODSHardware odsHardware;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        odsHardware = new ODSHardware();
        registerHardwareInterface("ods", odsHardware);

        promptAllianceColor();

        waitForStart();

        do {
            driveHardware.setMotorSpeeds(0.2, 0.2);
            waitOneFullHardwareCycle();
        } while (odsHardware.getLineColor().equals(ODSHardware.LineColor.DARK));
        waitMillis(200);
        driveHardware.stopMotors();

        alignWithWall();

        do {
            driveHardware.setMotorSpeeds(0.1, 0.1);
            waitOneFullHardwareCycle();
        } while (usHardware.getDistance() > 15);
        driveHardware.stopMotors();

        flipperHardware.dump();
    }
}
