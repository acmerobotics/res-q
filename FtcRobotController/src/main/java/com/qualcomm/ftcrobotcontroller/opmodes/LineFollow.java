package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.Palette;
import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.ftcrobotcontroller.data.PIDController;
import com.qualcomm.ftcrobotcontroller.data.TimestampedData;
import com.qualcomm.ftcrobotcontroller.hardware.drive.DriveHardware;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Admin on 2/16/2016.
 */
public class LineFollow extends LinearRobotController {

    DriveHardware driveHardware;
    ColorSensor colorSensor;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        driveHardware = new DriveHardware();
        registerHardwareInterface("drive", driveHardware);

        colorSensor = hardwareMap.colorSensor.get("line");
        colorSensor.setI2cAddress(0x3e);

        waitForStart();

        Palette.Color target = Palette.Color.BLUE;

        PIDController<Double> controller = new PIDController<Double>(0, 0.005, 0);
        double base = 0.4;
        while (opModeIsActive()) {
            Palette.Color current = Palette.getColorFromRGB(
                    colorSensor.red(),
                    colorSensor.green(),
                    colorSensor.blue()
            );
            if (current.equals(target)) {
                controller.add(new TimestampedData<Double>(0D));
            } else if (controller.getOutput() > 0) {
                controller.add(new TimestampedData<Double>(-1D));
            } else {
                controller.add(new TimestampedData<Double>(1D));
            }
            driveHardware.setMotorSpeeds(base + controller.getOutput(), base - controller.getOutput());
            telemetry.addData("Output", controller.getOutput());
            waitOneFullHardwareCycle();
        }

    }
}
