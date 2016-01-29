package com.qualcomm.ftcrobotcontroller.control;

import com.qualcomm.ftcrobotcontroller.hardware.HardwareInterface;

/**
 * Created by Admin on 1/28/2016.
 */
public interface Controller {
    public boolean registerHardwareInterface(String name, HardwareInterface hi);
    public boolean deregisterHardwareInterface(String name);
}
