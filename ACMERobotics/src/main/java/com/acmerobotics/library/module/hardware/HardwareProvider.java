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
            Device hardware = (Device) dependency.getAnnotation(Device.class);
            deviceName = hardware.value();
        } else {
            deviceName = name;
        }
        System.out.println("hardware provider:\t'" + deviceName + "'");
        System.out.println("hardware provider:\ttype " + type.getCanonicalName());
        for (Field field : HardwareMap.class.getFields()) {
            if (field.getName().toLowerCase().equals(typeName)) {
                System.out.println("hardware provider:\tmatch " + field.getName());
                try {
                    return ((HardwareMap.DeviceMapping<T>) field.get(hardwareInjector.getHardwareMap())).get(deviceName);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("hardware provider:\tno match");
        return null;
    }
}
