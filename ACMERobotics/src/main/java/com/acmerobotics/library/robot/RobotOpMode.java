package com.acmerobotics.library.robot;

import android.os.SystemClock;
import android.util.Log;

import com.acmerobotics.library.module.hardware.HardwareInjector;
import com.acmerobotics.library.module.hardware.HardwareModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public abstract class RobotOpMode<Config extends RobotConfig> extends OpMode {

    protected String loggingTag = "OpMode";

    protected Config robot;
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
        Class<Config> configClass = (Class<Config>) this.getClass().getAnnotation(RobotClass.class).value();
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
