package com.qualcomm.ftcrobotcontroller.hardware.sensors;

import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.ftcrobotcontroller.hardware.HardwareInterface;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.util.RobotLog;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;

/**
 * Created by Ryan on 11/29/2015.
 */
public abstract class I2cHardware extends HardwareInterface {

    public int i2cPort, i2cAddress;

    public DeviceInterfaceModule dim;
    public I2cController controller;
    public OpMode opMode;

    public int lastReadAddress = -1;
    public int lastReadLength = 0;
    public I2cReadCallback lastReadCallback = null;

    public byte[] temp;

    public interface I2cReadCallback {
        public void onReadFinished(int address, byte[] result, int length);
    }

    public static String byteArrayToString(byte[] arr) {
        String s = "";
        for (int i = 0; i < arr.length; i++) {
            s += String.format("%02x ", arr[i]);
        }
        return s;
    }

    public static int assembleWord(byte lsb, byte msb) {
        return (lsb & 0xff) | ((msb & 0xff) << 8); // the '& 0xff' converts from signed to unsigned byte
    }

    @Override
    public void init(OpMode mode) {
        i2cPort = getI2cPort();
        i2cAddress = getI2cAddress();

        opMode = mode;

        dim = mode.hardwareMap.deviceInterfaceModule.get("dim");
        controller = (I2cController) dim;
    }

    @Override
    public void loop(double timeSinceLastLoop) {
        if (isReading() && controller.isI2cPortReady(i2cPort) && !controller.isI2cPortActionFlagSet(i2cPort)) {
            controller.readI2cCacheFromController(i2cPort);
            Lock readLock = controller.getI2cReadCacheLock(i2cPort);
            try {
                readLock.lockInterruptibly();
                byte[] readCache = controller.getI2cReadCache(i2cPort);
                byte[] result = Arrays.copyOfRange(readCache,
                        I2cController.I2C_BUFFER_START_ADDRESS,
                        I2cController.I2C_BUFFER_START_ADDRESS + lastReadLength);
                lastReadCallback.onReadFinished(lastReadAddress, result, lastReadLength);
                resetRead();
            } catch (InterruptedException e) {
                RobotLog.e(e.getMessage());
            } finally {
                readLock.unlock();
            }
        }
    }

    @Override
    public String getStatusString() {
        return "";
        //return "name: " + controller.getDeviceName() + "  port: " + i2cPort + "  address: " + Integer.toHexString(i2cAddress) + "  info: " + controller.getConnectionInfo();
    }

    public abstract int getI2cPort();
    public abstract int getI2cAddress();

    public boolean isReading() {
        return lastReadAddress != -1;
    }

    public void writeRegisterSync(int address, byte b) {
        waitForReady();

        controller.enableI2cWriteMode(i2cPort, i2cAddress, address, 1);

        Lock lock = controller.getI2cWriteCacheLock(i2cPort);
        try {
            lock.lock();
            byte[] cache = controller.getI2cWriteCache(i2cPort);
            cache[I2cController.I2C_BUFFER_START_ADDRESS] = b;
        } finally {
            lock.unlock();
        }

        controller.setI2cPortActionFlag(i2cPort);
        controller.writeI2cCacheToController(i2cPort);

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            RobotLog.e(e.getMessage());
        }
    }

    public void readRegister(int address, int length, I2cReadCallback cb) {
        if (isReading()) return;

        waitForReady();

        controller.enableI2cReadMode(i2cPort, i2cAddress, address, length);

        controller.writeI2cCacheToController(i2cPort);

        lastReadAddress = address;
        lastReadLength = length;
        lastReadCallback = cb;
    }

    public byte[] readRegisterSync(int address, int length) {
        if (!(opMode instanceof LinearRobotController)) { return null; }
        temp = null;
        readRegister(address, length, new I2cReadCallback() {
            @Override
            public void onReadFinished(int address, byte[] result, int length) {
                temp = result;
            }
        });
        while (temp == null) {
            try {
                ((LinearRobotController) opMode).waitOneFullHardwareCycle();
            } catch (InterruptedException e) {
                RobotLog.e(e.getMessage());
            }
        }
        return temp;
    }

    public void waitForReady() {
        while(!controller.isI2cPortReady(i2cPort)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                RobotLog.e(e.getMessage());
            }
        }
    }

    public void resetRead() {
        lastReadAddress = -1;
        lastReadLength = 0;
        lastReadCallback = null;
    }
}
