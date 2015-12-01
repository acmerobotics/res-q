package com.qualcomm.ftcrobotcontroller.hardware;

import com.qualcomm.ftcrobotcontroller.util.Helper;
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

    public int lastReadAddress = -1;
    public int lastReadLength = 0;
    public I2cReadCallback lastReadCallback = null;

    public interface I2cReadCallback {
        public void onReadFinished(int address, byte[] result, int length);
    }

    @Override
    public void init(OpMode mode) {
        i2cPort = getI2cPort();
        i2cAddress = getI2cAddress();

        dim = mode.hardwareMap.deviceInterfaceModule.get("dim");
        controller = (I2cController) dim;

        if (controller != null) {
            if (Helper.DEBUG) RobotLog.d("Connection Info: " + controller.getConnectionInfo());
        } else {
            if (Helper.DEBUG) RobotLog.d("Device failed to initialize");
        }
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

    public abstract int getI2cPort();
    public abstract int getI2cAddress();

    public boolean isReading() {
        return lastReadAddress != -1;
    }

    public void writeRegisterSync(int address, byte b) {
        waitForReady();
        if (Helper.DEBUG) RobotLog.d("I2C port " + i2cPort + " is ready");

        controller.enableI2cWriteMode(i2cPort, i2cAddress, address, 1);

        Lock lock = controller.getI2cWriteCacheLock(i2cPort);
        try {
            lock.lock();
            byte[] cache = controller.getI2cWriteCache(i2cPort);
            cache[I2cController.I2C_BUFFER_START_ADDRESS] = b;
            if (Helper.DEBUG) RobotLog.d("Write: " + Helper.byteArrayToString(cache));
        } finally {
            lock.unlock();
        }

        controller.setI2cPortActionFlag(i2cPort);
        controller.writeI2cCacheToController(i2cPort);

        Helper.wait(250);
    }

    public void readRegister(int address, int length, I2cReadCallback cb) {
        waitForReady();

        if (Helper.DEBUG) RobotLog.d("I2C port " + i2cPort + " is ready");

        controller.enableI2cReadMode(i2cPort, i2cAddress, address, length);

        controller.setI2cPortActionFlag(i2cPort);
        controller.writeI2cCacheToController(i2cPort);

        lastReadAddress = address;
        lastReadLength = length;
        lastReadCallback = cb;
    }

    public void waitForReady() {
        while(!controller.isI2cPortReady(i2cPort)) {
            Helper.wait(100);
        }
    }

    public void resetRead() {
        RobotLog.d("Reset read");
        lastReadAddress = -1;
        lastReadLength = 0;
        lastReadCallback = null;
    }
}
