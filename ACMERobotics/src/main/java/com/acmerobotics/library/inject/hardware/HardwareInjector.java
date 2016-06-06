package com.acmerobotics.library.inject.hardware;

import com.acmerobotics.library.inject.core.BaseModule;
import com.acmerobotics.library.inject.core.Injector;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class HardwareInjector extends Injector {

    private HardwareMap hardwareMap;

    public HardwareInjector(BaseModule baseModule, HardwareMap map) {
        super(baseModule);
        hardwareMap = map;
    }

    public HardwareMap getHardwareMap() {
        return hardwareMap;
    }

}
