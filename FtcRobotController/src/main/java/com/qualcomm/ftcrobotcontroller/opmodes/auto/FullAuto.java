package com.qualcomm.ftcrobotcontroller.opmodes.auto;

import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.ftcrobotcontroller.hardware.drive.DriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.drive.GyroDriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.ArmHardware;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.PuncherHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.I2cGyroHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.I2cColorHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.UltrasonicPairHardware;

/**
 * Created by Ryan on 12/10/2015.
 */
public class FullAuto extends LinearRobotController {

    public static double LENGTH1 = 91.44, LENGTH2 = 86.21, LENGTH3 = 6.0;

    private DriveHardware driveHardware;
    private GyroDriveHardware gyroDriveHardware;
    private I2cGyroHardware gyroHardware;
    private UltrasonicPairHardware usHardware;
    private I2cColorHardware colorHardware;
    private PuncherHardware puncherHardware;
    private ArmHardware armHardware;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        driveHardware = new DriveHardware();
        gyroHardware = new I2cGyroHardware();
        gyroDriveHardware = new GyroDriveHardware(driveHardware, gyroHardware);

        usHardware = new UltrasonicPairHardware();
        colorHardware = new I2cColorHardware();
        puncherHardware = new PuncherHardware();
        armHardware = new ArmHardware();

        registerHardwareInterface("drive", driveHardware);
        registerHardwareInterface("gyro", gyroHardware);
        registerHardwareInterface("gyro_drive", gyroDriveHardware);
        registerHardwareInterface("us", usHardware);
        registerHardwareInterface("color", colorHardware);
        registerHardwareInterface("pusher", puncherHardware);
        registerHardwareInterface("arm", armHardware);

        promptAllianceColor();

        waitForStart();

        do {
            driveHardware.setMotorSpeeds(0.2, 0.2);
            waitOneFullHardwareCycle();
        } while (usHardware.getDistance() > LENGTH2);
        driveHardware.stopMotors();

        gyroDriveHardware.turnRightSync(45);

        // experimental
        double diff, speed;
        do {
            telemetry.addData("Status", "Lining Up");
            diff = usHardware.getDifference();
            speed = diff * 0.025;
            driveHardware.setMotorSpeeds(-speed, speed);
            waitOneFullHardwareCycle();
        } while (Math.abs(diff) > 2.0);
        // end experimental

        do {
            driveHardware.setMotorSpeeds(0.15, 0.15);
            waitOneFullHardwareCycle();
        } while (usHardware.getDistance() > LENGTH3);
        driveHardware.stopMotors();

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
    }
}
