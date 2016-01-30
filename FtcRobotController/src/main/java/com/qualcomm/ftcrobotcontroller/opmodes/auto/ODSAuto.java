package com.qualcomm.ftcrobotcontroller.opmodes.auto;

import com.qualcomm.ftcrobotcontroller.hardware.sensors.ODSHardware;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

/**
 * Created by Admin on 1/26/2016.
 */
public class ODSAuto extends Auto {

    protected ODSHardware odsHardware;
    
    public enum LineSide {
        LEFT,
        RIGHT
    }

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        odsHardware = new ODSHardware();
        registerHardwareInterface("ods", odsHardware);

        promptAllianceColor();

        waitForStart();

        while (odsHardware.getLineColor().equals(ODSHardware.LineColor.DARK)) {
            driveHardware.setMotorSpeeds(0.3, 0.3);
            waitOneFullHardwareCycle();
        }
        driveHardware.stopMotors();

        //smartDriveHardware.turnRightSync(20);

        LineSide currentSide = LineSide.LEFT;
        boolean centered = true;
        // experimental
        double speed, lineError, usError, base;
        do {
            if (odsHardware.getLineColor().equals(ODSHardware.LineColor.LIGHT)) {
                lineError = 0;
                centered = true;
            } else {
                lineError = driveHardware.movingLeft() ? 1 : -1;
            }
            usError = usHardware.getDifference();
//            speed = usError * 0.05 + lineError * (usHardware.getDistance() - 15) / 150;
//            if (usHardware.getDistance() > 15) {
//                speed += lineError * 0.2;
//            }

            base = (usHardware.getDistance() - 10.0) / 300.0;
            if (!centered) {
                speed = 0.5 * lineError;
                base = 0;
            } else {
                speed = usError * 0.05;
            }
            driveHardware.setMotorSpeeds(base + speed, base - speed);
            telemetry.clearData();
            telemetry.addData("base", base);
            telemetry.addData("usError", usError);
            telemetry.addData("speed", speed);
            waitOneFullHardwareCycle();
        } while (usHardware.getDistance() > 15 || Math.abs(usHardware.getDifference()) > 1.5);
        // end experimental
        driveHardware.stopMotors();

//        this.alignWithWall();
//
//        do {
//            driveHardware.setMotorSpeeds(0.2, 0.2);
//            waitOneFullHardwareCycle();
//        } while (usHardware.getDistance() > 10);
//        driveHardware.stopMotors();

        flipperHardware.dump();

        this.pushButtons();
    }
}
