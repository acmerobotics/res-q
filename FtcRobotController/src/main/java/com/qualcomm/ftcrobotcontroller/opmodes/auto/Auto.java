package com.qualcomm.ftcrobotcontroller.opmodes.auto;

import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.ftcrobotcontroller.hardware.drive.DriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.drive.SmartDriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.ArmHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.FlipperHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.PuncherHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.IMUHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.ColorHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.UltrasonicPairHardware;

/**
 * Created by Ryan on 12/10/2015.
 */
public class Auto extends LinearRobotController {

    protected DriveHardware driveHardware;
    protected SmartDriveHardware smartDriveHardware;
    protected IMUHardware gyroHardware;
    protected UltrasonicPairHardware usHardware;
    protected ColorHardware colorHardware;
    protected PuncherHardware puncherHardware;
    protected ArmHardware armHardware;
    protected FlipperHardware flipperHardware;

    public static double cm(double in) {
        return in * 2.54;
    }

    protected void alignWithWall() throws InterruptedException {
        // experimental
        double diff, speed;
        do {
            diff = usHardware.getDifference();
            speed = diff * -0.025;
            driveHardware.setMotorSpeeds(-speed, speed);
            waitOneFullHardwareCycle();
        } while (Math.abs(diff) > 1.0);
        // end experimental
    }

    protected void pushButtons() {
        ColorHardware.Color color;
        do {
            color = colorHardware.getPredominantColor();
        } while (color != ColorHardware.Color.BLUE && color != ColorHardware.Color.RED);

        if (color.toString().equals(getAllianceColor().toString())) {
            // right side
            puncherHardware.punchRight();
        } else {
            // left side
            puncherHardware.punchLeft();
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        driveHardware = new DriveHardware();
        gyroHardware = new IMUHardware();
        smartDriveHardware = new SmartDriveHardware(driveHardware, gyroHardware);

        usHardware = new UltrasonicPairHardware();
        colorHardware = new ColorHardware();
        puncherHardware = new PuncherHardware();
        armHardware = new ArmHardware();
        flipperHardware = new FlipperHardware();

        registerHardwareInterface("drive", driveHardware);
        registerHardwareInterface("gyro", gyroHardware);
        registerHardwareInterface("gyro_drive", smartDriveHardware);
        registerHardwareInterface("us", usHardware);
        registerHardwareInterface("color", colorHardware);
        registerHardwareInterface("pusher", puncherHardware);
        registerHardwareInterface("arm", armHardware);
        registerHardwareInterface("flipper", flipperHardware);
    }
}
