package com.qualcomm.ftcrobotcontroller.opmodes.auto;

import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

/**
 * Created by Admin on 1/26/2016.
 */
public class ODSAuto extends Auto {

    protected OpticalDistanceSensor odsSensor;
    
    public enum LineSide {
        LEFT,
        RIGHT
    }

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        odsSensor = hardwareMap.opticalDistanceSensor.get("ods");

        waitForStart();

        while (odsSensor.getLightDetected() < 0.5) {
            driveHardware.setMotorSpeeds(0.3, 0.3);
            waitOneFullHardwareCycle();
        }
        driveHardware.stopMotors();

        LineSide side = LineSide.RIGHT;
        boolean centered = true;

        // experimental
        double speed, lineError;
        do {
            if (odsSensor.getLightDetected() > 0.5) {
                lineError = 0;
                centered = true;
            } else if (side.equals(LineSide.LEFT)) {
                if (centered) {
                    side = LineSide.RIGHT;
                    centered = false;
                }
                lineError = -25;
            } else {
                if (centered) {
                    side = LineSide.LEFT;
                    centered = false;
                }
                lineError = 25;
            }
            speed = lineError * -0.025;
            driveHardware.setMotorSpeeds(-speed, speed);
            waitOneFullHardwareCycle();
        } while (usHardware.getDistance() > 13);
        // end experimental
    }
}
