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

//        LineSide currentSide = LineSide.LEFT;
//        boolean centered = true;
//        // experimental
//        double speed, error, lineError, usError, base;
//        do {
//            if (odsHardware.getLineColor().equals(ODSHardware.LineColor.LIGHT)) {
//                centered = true;
//                lineError = 0;
//            } else {
//                if (centered) {
//                    centered = false;
//                    currentSide = currentSide.equals(LineSide.RIGHT) ? LineSide.LEFT : LineSide.RIGHT;
//                }
//                lineError = currentSide.equals(LineSide.RIGHT) ? 1 : -1;
//            }
//            usError = usHardware.getDifference();
//            speed = lineError * 0.2 + usError * 0.035;
//            base = (usHardware.getDistance() - 10.0) / 200.0;
//            driveHardware.setMotorSpeeds(base + speed, base - speed);
//            waitOneFullHardwareCycle();
//        } while (usHardware.getDistance() > 13 || Math.abs(usHardware.getDifference()) > 3);
//        // end experimental
//        driveHardware.stopMotors();

        this.alignWithWall();

        do {
            driveHardware.setMotorSpeeds(0.3, 0.3);
            waitOneFullHardwareCycle();
        } while (usHardware.getDistance() > 10);
        driveHardware.stopMotors();

        this.pushButtons();

        flipperHardware.dump();
    }
}
