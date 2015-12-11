package com.qualcomm.ftcrobotcontroller.control;

import android.graphics.Color;
import android.graphics.LinearGradient;

import com.qualcomm.ftcrobotcontroller.hardware.HardwareInterface;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.RobotLog;

import java.util.HashMap;

/**
 * Created by Admin on 10/5/2015.
 */


public class RobotController extends OpMode {
    private HashMap<String, HardwareInterface> hardwareInterfaces;
    private OpMode opMode;

    private AllianceColor color = AllianceColor.UNSET;

    public enum AllianceColor {
        BLUE,
        RED,
        UNSET
    }

    public RobotController() {
        opMode = this;
    }

    public RobotController(OpMode mode) {
        opMode = mode;
    }

    public void init() {
        hardwareInterfaces = new HashMap<String, HardwareInterface>();
    }

    public boolean registerHardwareInterface(String name, HardwareInterface hi) {
        if (hardwareInterfaces.containsKey(name)) {
            return false;
        } else {
            hardwareInterfaces.put(name, hi);
            hi.init(opMode);
            return true;
        }
    }

    public boolean deregisterHardwareInterface(String name) {
        if (hardwareInterfaces.containsKey(name)) {
            hardwareInterfaces.remove(name);
            return true;
        } else {
            return false;
        }
    }

    public void loop() {
        if (isAllianceColorSet()) telemetry.addData("Alliance Color", getAllianceColor());
        for (String name : hardwareInterfaces.keySet()) {
            hardwareInterfaces.get(name).loop(getRuntime());
        }
        resetStartTime();
    }

    public void promptAllianceColor() {
        while (true) {
            telemetry.addData("Alliance Color", "Press [X] for blue or [B] for red");
            if (gamepad1.x) {
                // blue
                this.color = AllianceColor.BLUE;
                break;
            } else if (gamepad1.b) {
                // red
                this.color = AllianceColor.RED;
                break;
            }
        }
    }

    public AllianceColor getAllianceColor() {
        return color;
    }

    public boolean isAllianceColorSet() {
        return !color.equals(AllianceColor.UNSET);
    }
}
