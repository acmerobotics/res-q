package com.qualcomm.ftcrobotcontroller.opmodes.auto;

import com.qualcomm.ftcrobotcontroller.data.PIDController;
import com.qualcomm.ftcrobotcontroller.data.TimestampedData;

/**
 * Created by Admin on 1/26/2016.
 */
public class SmartAuto extends Auto {

    /*
        PARAMETERS
        ==========
        BASE_SPEED: Controls the robot speed near the beacon.
        SPEED_INCR: Controls the robot speed up until the beacon.
        WALL_P:     Controls how fast the robot turns to align with the wall.
        LINE_P:     Controls how fast the robot turns to find the line again.
        WALL_DISTANCE: The distance between the robot and the wall at dumping time.
        DIFF_BOUND: An acceptable ultrasonic sensor difference for termination.

        CONFIGURATIONS
        ==============
        NORMAL: TODO fill this in with actual results!
        1:
    */

    public static final double BASE_SPEED       = 0.06,
                               SPEED_INCR       = 0.006,
                               WALL_P           = -0.05,
                               LINE_P           = 0.01,
                               WALL_DISTANCE    = 15,
                               DIFF_BOUND       = 1.5;

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

        boolean centered = true;
        double baseSpeed, speedDiff, usDist, usDiff, lineError;
        do {
            usDiff = usHardware.getDifference();
            usDist = usHardware.getDistance();

            if (getLineColor().equals(LineColor.LIGHT)) {
                gyroSensor.resetZAxisIntegrator();
                centered = true;
                lineError = 0;
            } else {
                lineError = gyroSensor.getHeading();
                if (lineError > 180) {
                    lineError -= 360;
                }
                centered = false;
            }

            baseSpeed = BASE_SPEED + SPEED_INCR * (usDist - WALL_DISTANCE);
            speedDiff = WALL_P * usDiff;

            telemetry.addData("line", getLineStrength());

            if (centered) {
                telemetry.addData("status", "moving forward");
                driveHardware.setMotorSpeeds(baseSpeed - speedDiff, baseSpeed + speedDiff);
            } else {
                telemetry.addData("status", "lining up");
                driveHardware.setMotorSpeeds(LINE_P * lineError, -LINE_P * lineError);
            }
            waitOneFullHardwareCycle();
        } while (usHardware.getDistance() > WALL_DISTANCE || Math.abs(usHardware.getDifference()) > DIFF_BOUND);

        driveHardware.stopMotors();

        flipperHardware.dump();

        this.pushButtons();
    }
}
