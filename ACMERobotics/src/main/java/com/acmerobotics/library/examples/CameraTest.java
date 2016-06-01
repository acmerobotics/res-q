package com.acmerobotics.library.examples;

import android.util.Log;

import com.acmerobotics.library.camera.FrameProcessor;
import com.acmerobotics.library.camera.FtcCamera;
import com.acmerobotics.library.robot.RobotClass;
import com.acmerobotics.library.robot.RobotOpMode;

@RobotClass(CameraTest.Config.class)
public class CameraTest extends RobotOpMode<CameraTest.Config> {

    public static class Config {
        FtcCamera camera;
    }

    @Override
    public void init() {
        super.init();

        robot.camera.setFrameProcessor(new FrameProcessor.SquareCorners());
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
