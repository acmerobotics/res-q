package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.hardware.FSMManager;
import com.qualcomm.ftcrobotcontroller.hardware.GyroDriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.RobotController;

/**
 * Created by Admin on 12/6/2015.
 */
public class FSMAutoTest extends RobotController {

    public FSMManager<AutoState> manager;

    public GyroDriveHardware gyroDriveHardware;

    private enum AutoState {
        INIT,
        TURN,
        STOP
    }

    @Override
    public void init() {
        super.init();

        gyroDriveHardware = new GyroDriveHardware();
        registerHardwareInterface("gyro_drive", gyroDriveHardware);

        manager = new FSMManager<AutoState>(new FSMManager.FSMStateCallback<AutoState>() {
            @Override
            public AutoState onInit() {
                return AutoState.INIT;
            }

            @Override
            public void onLoop(AutoState currentState, double timeSinceLastLoop) {

            }

            @Override
            public void onStart(AutoState newState) {
                switch(newState) {
                    case INIT:
                        manager.changeState(AutoState.TURN);
                    case TURN:
                        gyroDriveHardware.turnLeft(90, new GyroDriveHardware.TurnCallback() {
                            @Override
                            public void onTurnFinished() {
                                manager.changeState(AutoState.STOP);
                            }
                        });
                        break;
                }
            }

            @Override
            public void onStop(AutoState oldState) {
                switch(oldState) {
                    case TURN:

                }
            }
        });
        registerHardwareInterface("manager", manager);
    }

    @Override
    public void loop() {
        super.loop();
    }
}
