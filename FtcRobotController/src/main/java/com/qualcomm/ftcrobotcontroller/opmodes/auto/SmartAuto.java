package com.qualcomm.ftcrobotcontroller.opmodes.auto;

import com.qualcomm.ftcrobotcontroller.data.PIDController;
import com.qualcomm.ftcrobotcontroller.data.TimestampedData;

/**
 * Created by Admin on 1/26/2016.
 */
public class SmartAuto extends Auto {

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

        PIDController<Double> controller = new PIDController(0.01, 0.005, 0);
        while (usHardware.getDistance() > 16) {
            double base = 0.1, reading;
            if (getLineColor() == LineColor.DARK) {
                if (usHardware.getDifference() < 0) {
                    reading = 1D;
                } else {
                    reading = -1D;
                }
            } else {
                reading = 0D;
            }
            controller.add(new TimestampedData<Double>(reading));
            double output = controller.getOutput();
            driveHardware.setMotorSpeeds(base + output, base - output);
        }

        flipperHardware.dump();

        this.pushButtons();
    }
}
