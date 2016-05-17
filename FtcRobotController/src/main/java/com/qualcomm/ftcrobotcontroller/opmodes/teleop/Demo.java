package com.qualcomm.ftcrobotcontroller.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Admin on 1/20/2016.
 */
public class Demo extends TeleOp {
    @Override
    public void init() {
        super.init();
        setOperationMode(OperationMode.DEMO);
    }
}
