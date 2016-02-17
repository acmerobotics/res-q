package com.qualcomm.ftcrobotcontroller.opmodes.auto;

import com.qualcomm.ftcrobotcontroller.data.PIDController;
import com.qualcomm.ftcrobotcontroller.data.TimestampedData;

/**
 * Created by Admin on 1/26/2016.
 */
public class SmartAuto extends Auto {

    public enum LineSide {
        LEFT,
        RIGHT
    }

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        promptAllianceColor();

        waitForStart();

        while (getLineColor() == LineColor.DARK) {
            smartDriveHardware.driveStraight();
            waitOneFullHardwareCycle();
        }
        smartDriveHardware.stopMotors();

        LineSide currentSide = LineSide.LEFT;
        boolean centered = true;
        // experimental
        double speed, lineError, usError, base;
        do {
            if (getLineColor().equals(LineColor.LIGHT)) {
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
            waitOneFullHardwareCycle();
        } while (usHardware.getDistance() > 15 || Math.abs(usHardware.getDifference()) > 1.5);
        // end experimental
        driveHardware.stopMotors();

        flipperHardware.dump();

        this.pushButtons();
    }
}
