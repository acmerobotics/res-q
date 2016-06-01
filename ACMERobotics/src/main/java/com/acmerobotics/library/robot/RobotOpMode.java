package com.acmerobotics.library.robot;

import android.os.SystemClock;
import android.util.Log;

import com.acmerobotics.library.module.hardware.HardwareInjector;
import com.acmerobotics.library.module.hardware.HardwareModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public abstract class RobotOpMode<T> extends OpMode {

    protected String loggingTag = "OpMode";

    protected T robot;
    private HardwareInjector injector;

    public RobotOpMode() {

    }

    public String getLoggingTag() {
        return loggingTag;
    }

    public void setLoggingTag(String s) {
        loggingTag = s;
    }

    public void log(String msg) {
        Log.i(loggingTag, msg);
    }

    public void delay(int ms) {
        SystemClock.sleep(ms);
    }

    @Override
    public void init() {
        injector = new HardwareInjector(new HardwareModule(this), hardwareMap);
        Class<T> configClass = (Class<T>) this.getClass().getAnnotation(RobotClass.class).value();
        try {
            robot = configClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        injector.fill(robot);
    }

}
