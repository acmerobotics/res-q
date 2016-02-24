package com.qualcomm.ftcrobotcontroller.opmodes.auto;

import com.acmerobotics.library.data.TimestampedData;
import com.acmerobotics.library.file.DataLogger;
import com.acmerobotics.library.graph.PIDNode;
import com.acmerobotics.library.graph.ValueNode;

/**
 * Created by Admin on 1/26/2016.
 */
public class SmarterAuto extends Auto {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        PIDNode<Double> speedController = new PIDNode<Double>(0.5, 0, 0);
        ValueNode<Double> speedValue = new ValueNode<Double>(0D);
        DataLogger dataLogger = new DataLogger(this, "smarter_auto.csv");

        dataLogger.writeLine("timestamp,strength,diff");

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
        while (usHardware.getDistance() > 15) {
            double usDiff = usHardware.getDifference();

            double datum = getLineStrength() * sign(usDiff);

            double base = 0.1;

            speedController.update(TimestampedData.wrap(datum));

            double diff = speedValue.getLatestValue();

            driveHardware.setMotorSpeeds(base - diff, base + diff);

            dataLogger.writeLine(System.nanoTime() + "," + getLineStrength() + "," + diff);

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
