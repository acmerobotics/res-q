package com.acmerobotics.library.inject.hardware;

import android.app.Activity;
import android.content.Context;
import android.support.v8.renderscript.*;

import com.acmerobotics.library.inject.core.BaseModule;
import com.acmerobotics.library.robot.RobotOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.robot.Robot;

public class HardwareModule extends BaseModule {

    private RobotOpMode opMode;

    public HardwareModule(RobotOpMode mode) {
        opMode = mode;
    }

    @Override
    public void configure() {
        super.configure();

        Context context = opMode.hardwareMap.appContext;

        bindAll().withAnnotation(Hardware.class).withAnnotation(Device.class).toProvider(new HardwareProvider());
        bind(OpMode.class).toInstance(opMode);
        bind(RobotOpMode.class).toInstance(opMode);
        bind(Activity.class).toInstance(context);
        bind(Context.class).toInstance(context);
    }

}
