package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.hardware.GyroDriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.LinearRobotController;

/**
 * Created by Ryan on 12/6/2015.
 */
public class AutoTurn extends LinearRobotController {

    public GyroDriveHardware gyroDriveHardware;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        gyroDriveHardware = new GyroDriveHardware();

        waitForStart();

        gyroDriveHardware.turnLeft(90, new GyroDriveHardware.TurnCallback() {
            @Override
            public void onTurnFinished() {

            }
        });
    }
}
