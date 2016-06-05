package com.acmerobotics.library.examples;

import android.app.Activity;

import com.acmerobotics.library.robot.AlliancePrompt;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class AllianceTest extends OpMode {

    private AlliancePrompt.AllianceColor color;
    private AlliancePrompt prompt;

    @Override
    public void init() {
        prompt = new AlliancePrompt((Activity) hardwareMap.appContext);
        prompt.setAllianceSelectListener(new AlliancePrompt.AllianceSelectListener() {
            @Override
            public void onAllianceSelect(AlliancePrompt.AllianceColor color) {
                AllianceTest.this.color = color;
            }
        });
        prompt.show();
        color = null;
    }

    @Override
    public void loop() {
        if (color == null) {
            telemetry.addData("color", "none");
        } else {
            telemetry.addData("color", color.toString());
        }
    }
}
