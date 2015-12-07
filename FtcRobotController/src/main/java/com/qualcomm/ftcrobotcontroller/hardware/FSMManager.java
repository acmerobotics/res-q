package com.qualcomm.ftcrobotcontroller.hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Admin on 12/6/2015.
 */
public class FSMManager<T> extends HardwareInterface {

    private T currentState;
    private FSMStateCallback<T> callback;

    public interface FSMStateCallback<T> {
        public T onInit();
        public void onLoop(T currentState, double timeSinceLastLoop);
        public void onStart(T newState);
        public void onStop(T oldState);
    }

    public FSMManager(FSMStateCallback cb) {
        callback = cb;
        currentState = null;
    }

    @Override
    public void init(OpMode mode) {
        changeState(callback.onInit());
    }

    public void loop(double timeSinceLastLoop) {
        callback.onLoop(currentState, timeSinceLastLoop);
    }

    public void changeState(T newState) {
        if (currentState != null) callback.onStop(currentState);
        currentState = newState;
        callback.onStart(newState);
    }
}
