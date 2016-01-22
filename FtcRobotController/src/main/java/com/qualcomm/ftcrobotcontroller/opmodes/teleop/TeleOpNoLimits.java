package com.qualcomm.ftcrobotcontroller.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Admin on 1/20/2016.
 */
public class TeleOpNoLimits extends TeleOp {
    @Override
    public void init() {
        super.init();
        this.armHardware.setConstrained(false);
    }
}
