package com.qualcomm.ftcrobotcontroller.opmodes.auto;

import com.acmerobotics.library.data.TimestampedData;
import com.acmerobotics.library.graph.PIDNode;
import com.acmerobotics.library.graph.ValueNode;

/**
 * Created by Admin on 1/26/2016.
 */
public class SmarterAuto extends Auto {

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
    */

    /**
     * @param a number
     * @return -1 or 1 depending on the sign of a
     */
    public double sign(double a) {
        return a > 0 ? 1 : -1;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        PIDNode<Double> speedController = new PIDNode<Double>(0.1, 0.01, 0);
        ValueNode<Double> speedValue = new ValueNode<Double>();

        speedController.connect(speedValue);

        promptAllianceColor();

        waitForStart();

        // drive straight until the robot hits the white line
        while (getLineStrength() == 0) {
            smartDriveHardware.driveStraight();
            waitOneFullHardwareCycle();
        }
        smartDriveHardware.stopMotors();

        // line follow maybe?
        while (usHardware.getDifference() < 15) {
            double lineStrength = getLineStrength();
            double usDiff = usHardware.getDifference();
            double datum = (lineStrength - 5.0) * sign(usDiff);
            double base = 0.2;
            speedController.update(TimestampedData.wrap(datum));
            double diff = speedValue.getLatestValue();
            driveHardware.setMotorSpeeds(base - diff, base + diff);
            waitOneFullHardwareCycle();
        }
        driveHardware.stopMotors();

        // align with the wall
        this.alignWithWall();

        // dump the flippers
        flipperHardware.dump();

        // push the buttons
        this.pushButtons();
    }
}
