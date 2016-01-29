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

        LineSide currentSide = LineSide.LEFT;
        boolean centered = true;
        // experimental
        double speed, error, lineError, usError, base;
        do {
            if (odsHardware.getLineColor().equals(ODSHardware.LineColor.LIGHT)) {
                centered = true;
                lineError = 0;
            } else {
                if (centered) {
                    centered = false;
                    currentSide = currentSide.equals(LineSide.RIGHT) ? LineSide.LEFT : LineSide.RIGHT;
                }
                lineError = currentSide.equals(LineSide.RIGHT) ? 0.3 : -0.3;
            }
            usError = usHardware.getDifference();
            speed = lineError + usError * 0.035;
            base = (usHardware.getDistance() - 10.0) / 100.0;
            driveHardware.setMotorSpeeds(base + speed, base - speed);
            waitOneFullHardwareCycle();
        } while (usHardware.getDistance() > 15);
        // end experimental
        driveHardware.stopMotors();

        this.alignWithWall();

        this.pushButtons();

        flipperHardware.dump();
    }
}
