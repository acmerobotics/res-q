package com.qualcomm.ftcrobotcontroller.hardware;

import com.qualcomm.ftcrobotcontroller.util.Helper;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.util.RobotLog;

import java.util.concurrent.locks.Lock;

/**
 * Created by Admin on 11/30/2015.
 */
public class I2cRGBHardware extends I2cHardware {

    public static final int RGBC_TIME_ADDRESS = 0x01;
    public static final byte RGBC_TIME_DATA = (byte) 0xff; // set the integration
    public static final int ENABLE_ADDRESS = 0x00;
    public static final byte ENABLE_DATA_PON = (byte) 0x01;
    public static final byte ENABLE_DATA_AEN = (byte) 0x03;
    public static final int CONTROL_ADDRESS = 0x0f;
    public static final byte CONTROL_DATA = (byte) 0x00; // set the gain

    public static final int COMMAND_BIT = 0x80;

    public static final int CLEAR_DATA_ADDRESS = 0x14;

    private double clear = 0.0;
    private double red = 0.0;
    private double green = 0.0;
    private double blue = 0.0;

    public enum RGB {
        RED, GREEN, BLUE, NONE
    }

    public I2cReadCallback callback = new I2cReadCallback() {
        @Override
        public void onReadFinished(int address, byte[] latestData, int length) {
            clear = (double) Helper.assembleWord(latestData[0], latestData[1]);
            red = (double) Helper.assembleWord(latestData[2], latestData[3]);
            green = (double) Helper.assembleWord(latestData[4], latestData[5]);
            blue = (double) Helper.assembleWord(latestData[6], latestData[7]);
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
        writeRegisterSync(CONTROL_ADDRESS, CONTROL_DATA);

        writeRegisterSync(ENABLE_ADDRESS, ENABLE_DATA_PON);
        writeRegisterSync(ENABLE_ADDRESS, ENABLE_DATA_AEN);
    }

    @Override
    public void loop(double timeSinceLastLoop) {
        super.loop(timeSinceLastLoop);

        if (!isReading()) readRegister(CLEAR_DATA_ADDRESS, 8, callback);
    }

    @Override
    public void writeRegisterSync(int address, byte b) {
        super.writeRegisterSync(address | COMMAND_BIT, b);
    }

    @Override
    public void readRegister(int address, int length, I2cReadCallback cb) {
        super.readRegister(address | COMMAND_BIT, length, cb);
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

    public RGB getPredominantColor() {
        if (red > green && red > blue) {
            return RGB.RED;
        } else if (blue > green && blue > red) {
            return RGB.BLUE;
        } else if (green > blue && green > red) {
            return RGB.GREEN;
        }
        return RGB.NONE;
    }
}
