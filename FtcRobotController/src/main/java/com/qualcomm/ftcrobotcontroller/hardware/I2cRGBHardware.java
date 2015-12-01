package com.qualcomm.ftcrobotcontroller.hardware;

import com.qualcomm.ftcrobotcontroller.util.Helper;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * Created by Admin on 11/30/2015.
 */
public class I2cRGBHardware extends I2cHardware {

    public static final int RGBC_TIME_ADDRESS = 0x01;
    public static final byte RGBC_TIME_DATA = (byte) 0xff;
    public static final int CLEAR_DATA_ADDRESS = 0x14;

    public static final int MAX_COLOR = 0xffff;

    private double clear = 0.0;
    private double red = 0.0;
    private double green = 0.0;
    private double blue = 0.0;

    public I2cReadCallback callback = new I2cReadCallback() {
        @Override
        public void onReadFinished(int address, byte[] result, int length) {
            clear = Helper.assembleWord(result[0], result[1]) / MAX_COLOR;
            red = Helper.assembleWord(result[2], result[3]) / MAX_COLOR;
            green = Helper.assembleWord(result[4], result[5]) / MAX_COLOR;
            blue = Helper.assembleWord(result[6], result[7]) / MAX_COLOR;
        }
    };

    @Override
    public int getI2cPort() {
        return 1;
    }

    @Override
    public int getI2cAddress() {
        return 0x29 * 2;
    }

    @Override
    public void init(OpMode mode) {
        super.init(mode);

        writeRegisterSync(RGBC_TIME_ADDRESS, RGBC_TIME_DATA);
    }

    @Override
    public void loop(double timeSinceLastLoop) {
        super.loop(timeSinceLastLoop);

        if (!isReading()) readRegister(CLEAR_DATA_ADDRESS, 8, callback);
    }

    public double getClear() {
        return clear;
    }

    public double getRed() {
        return red;
    }

    public double getGreen() {
        return green;
    }

    public double getBlue() {
        return blue;
    }


}
