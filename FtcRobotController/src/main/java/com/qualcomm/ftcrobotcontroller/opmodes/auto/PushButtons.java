package com.qualcomm.ftcrobotcontroller.opmodes.auto;

import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.ftcrobotcontroller.hardware.drive.DriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.drive.SmartDriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.FlipperHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.PuncherHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.I2cColorHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.I2cIMUHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.UltrasonicPairHardware;

/**
 * Created by Ryan on 12/13/2015.
 */
public class PushButtons extends LinearRobotController {

    private I2cColorHardware colorHardware;
    private PuncherHardware puncherHardware;
    private FlipperHardware flipperHardware;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        colorHardware = new I2cColorHardware();
        puncherHardware = new PuncherHardware();
        flipperHardware = new FlipperHardware();

        registerHardwareInterface("color", colorHardware);
        registerHardwareInterface("punchers", puncherHardware);
        registerHardwareInterface("flipper", flipperHardware);

        promptAllianceColor();

        waitForStart();

        I2cColorHardware.Color color;
        do {
            color = colorHardware.getPredominantColor();
        } while (color != I2cColorHardware.Color.BLUE && color != I2cColorHardware.Color.RED);

        if (color.toString().equals(getAllianceColor().toString())) {
            // right side
            puncherHardware.punchRight();
        } else {
            // left side
            puncherHardware.punchLeft();
        }

        flipperHardware.dump();
    }
}
