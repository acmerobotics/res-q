package com.acmerobotics.library.module.hardware;

import com.acmerobotics.library.module.core.BaseModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class HardwareModule extends BaseModule {

    private OpMode opMode;

    public HardwareModule(OpMode mode) {
        opMode = mode;
    }

    @Override
    public void configure() {
        super.configure();

        bindAll().withAnnotation(Hardware.class).toProvider(new HardwareProvider());
        bind(OpMode.class).toInstance(opMode);
    }

}
