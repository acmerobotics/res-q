package com.qualcomm.ftcrobotcontroller.hardware;

import com.qualcomm.ftcrobotcontroller.opmodes.I2cTest;
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
public class I2cHardware extends HardwareInterface {

    public int port = 0;
    public int address = 0x28*2;

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
        if (lastReadAddress != -1 && controller.isI2cPortReady(port) && !controller.isI2cPortActionFlagSet(port)) {
            controller.readI2cCacheFromController(port);
            Lock readLock = controller.getI2cReadCacheLock(port);
            try {
                readLock.lockInterruptibly();
                byte[] readCache = controller.getI2cReadCache(port);
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

    public boolean isReading() {
        return lastReadAddress != -1;
    }

    public void writeRegisterSync(int address, byte b) {
        waitForReady();
        if (Helper.DEBUG) RobotLog.d("I2C port is ready");

        controller.enableI2cWriteMode(port, address, address, 1);

        Lock lock = controller.getI2cWriteCacheLock(port);
        try {
            lock.lock();
            byte[] cache = controller.getI2cWriteCache(port);
            cache[I2cController.I2C_BUFFER_START_ADDRESS] = b;
            if (Helper.DEBUG) RobotLog.d("Write: " + Helper.byteArrayToString(cache));
        } finally {
            lock.unlock();
        }

        controller.setI2cPortActionFlag(port);
        controller.writeI2cCacheToController(port);

        Helper.wait(250);
    }

    public void readRegister(int address, int length, I2cReadCallback cb) {
        waitForReady();

        if (Helper.DEBUG) RobotLog.d("I2C port is ready");

        controller.enableI2cReadMode(port, address, address, length);

        controller.setI2cPortActionFlag(port);
        controller.writeI2cCacheToController(port);

        lastReadAddress = address;
        lastReadLength = length;
        lastReadCallback = cb;
    }

    public void waitForReady() {
        RobotLog.d(port + " " + address);
        while(!controller.isI2cPortReady(port)) {
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
