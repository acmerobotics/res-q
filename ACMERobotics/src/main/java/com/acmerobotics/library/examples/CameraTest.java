package com.acmerobotics.library.examples;

import com.acmerobotics.library.camera.CameraSource;
import com.acmerobotics.library.robot.RobotClass;
import com.acmerobotics.library.robot.RobotOpMode;

@RobotClass(CameraTest.Config.class)
public class CameraTest extends RobotOpMode<CameraTest.Config> {

    public static class Config {
        CameraSource camera;
    }

    @Override
    public void init() {
        super.init();

        //robot.camera.pipeline().add(new MonoFilter(RenderScript.create(hardwareMap.appContext))).end();
        robot.camera.begin();
    }

    @Override
    public void loop() {

    }

    @Override
    public void stop() {
        robot.camera.releaseCamera();
    }

}
