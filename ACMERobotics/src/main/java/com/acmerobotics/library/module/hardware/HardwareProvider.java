package com.acmerobotics.library.module.hardware;

import com.acmerobotics.library.module.core.Dependency;
import com.acmerobotics.library.module.core.Injector;
import com.acmerobotics.library.module.core.Provider;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.lang.reflect.Field;

public class HardwareProvider<T> implements Provider<T> {

    private String name = null;

    public HardwareProvider() {

    }

    public HardwareProvider(String n) {
        name = n;
    }

    @Override
    public T provide(Injector injector, Dependency dependency) {
        HardwareInjector hardwareInjector = (HardwareInjector) injector;
        Class<T> type = dependency.getType();
        String typeName = type.getSimpleName().toLowerCase();
        String deviceName;
        if (name == null) {
            Hardware hardware = (Hardware) dependency.getAnnotation(Hardware.class);
            deviceName = hardware.value();
        } else {
            deviceName = name;
        }
        for (Field field : HardwareMap.class.getFields()) {
            if (field.getName().toLowerCase().equals(typeName)) {
                try {
                    System.out.println("hardware: " + deviceName);
                    return ((HardwareMap.DeviceMapping<T>) field.get(hardwareInjector.getHardwareMap())).get(deviceName);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
