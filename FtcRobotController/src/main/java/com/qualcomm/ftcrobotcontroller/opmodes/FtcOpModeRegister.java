/* Copyright (c) 2014, 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.auto.Auto;
import com.qualcomm.ftcrobotcontroller.opmodes.auto.DumbAuto;
import com.qualcomm.ftcrobotcontroller.opmodes.auto.SmartAuto;
import com.qualcomm.ftcrobotcontroller.opmodes.teleop.TeleOp;
import com.qualcomm.ftcrobotcontroller.opmodes.teleop.TeleOpNoLimits;
import com.qualcomm.ftcrobotcontroller.opmodes.test.FlipperTest;
import com.qualcomm.ftcrobotcontroller.opmodes.test.GyroTurnTest;
import com.qualcomm.ftcrobotcontroller.opmodes.test.I2cColorTest;
import com.qualcomm.ftcrobotcontroller.opmodes.test.I2cGyroTest;
import com.qualcomm.ftcrobotcontroller.opmodes.test.ODSTest;
import com.qualcomm.ftcrobotcontroller.opmodes.test.PuncherTest;
import com.qualcomm.ftcrobotcontroller.opmodes.test.UltrasonicTest;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;

/**
 * Register Op Modes
 */
public class FtcOpModeRegister implements OpModeRegister {

    public static final boolean TEST = false;

    /**
     * The Op Mode Manager will call this method when it wants a list of all
     * available op modes. Add your op mode to the list to enable it.
     *
     * @param manager op mode manager
     */
    public void register(OpModeManager manager) {
        /*
         * register your op modes here.
         * The first parameter is the name of the op mode
         * The second parameter is the op mode class property
         *
         * If two or more op modes are registered with the same name, the app will display an error.
         */

//        manager.register("TankDrive", TankDrive.class);
//        manager.register("ArcadeDrive", ArcadeDrive.class);

        if (TEST) {
            manager.register("FlipperTest", FlipperTest.class);
            manager.register("I2cGyroTest", I2cGyroTest.class);
            manager.register("I2cColorTest", I2cColorTest.class);
            manager.register("UltrasonicTest", UltrasonicTest.class);
            manager.register("GyroTurnTest", GyroTurnTest.class);
            manager.register("PuncherTest", PuncherTest.class);
            manager.register("ODS Test", ODSTest.class);
        }
        manager.register("TeleOp", TeleOp.class);
        manager.register("TeleOp (No Limits)", TeleOpNoLimits.class);
        manager.register("Smart Auto", SmartAuto.class);
        manager.register("Line Follow", LineFollow.class);
        manager.register("HSV", HSV.class);

        manager.register("PushButtons", new Auto() {
            @Override
            public void runOpMode() throws InterruptedException {
                super.runOpMode();
                promptAllianceColor();
                waitForStart();
                pushButtons();
            }
        });

        manager.register("Dumb Auto", DumbAuto.class);

        manager.register("Straight", new Auto() {
            @Override
            public void runOpMode() throws InterruptedException {
                super.runOpMode();
                waitForStart();
                smartDriveHardware.driveStraight();
                while(opModeIsActive()) {
                    waitOneFullHardwareCycle();
                }
            }
        });
    }
}