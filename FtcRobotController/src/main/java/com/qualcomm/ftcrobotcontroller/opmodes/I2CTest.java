package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.hardware.RobotController;
import com.qualcomm.ftcrobotcontroller.util.Helper;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.util.RobotLog;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;

/**
 * Created by Ryan on 11/23/2015.
 */
public class I2cTest extends RobotController {

    public static final boolean DEBUG = false;

    public static final int I2C_PORT = 0;
    public static final int I2C_ADDRESS = 0x28 * 2;
    public static final int OPR_MODE_ADDRESS = 0x3d;
    public static final byte OPR_MODE_DATA = 8;
    public static final int UNIT_SEL_ADDRESS = 0x80;
    public static final byte UNIT_SEL_DATA = 0;
    public static final int EUL_DATA_X_ADDRESS = 0x1a;

    public DeviceInterfaceModule dim;
    public I2cController controller;

    public int lastReadAddress = -1;
    public int lastReadLength = 0;
    public I2cReadCallback lastReadCallback = null;
    
    public long loopTime = 0;
    public long deltaTime = 0;

    public double lastHeading = 0.0;

    public interface I2cReadCallback {
        public void onReadFinished(int address, byte[] result, int length);
    }

    public I2cReadCallback callback = new I2cReadCallback() {
        @Override
        public void onReadFinished(int address, byte[] result, int length) {
            if (address == EUL_DATA_X_ADDRESS) {
                byte lsb = result[0];
                byte msb = result[1];
                int val =  (lsb & 0xff) | ((msb & 0xff) << 8); // the '& 0xff' converts from signed to unsigned byte
                double heading = ((double) val) / 16.0;
                lastHeading = heading;
                RobotLog.d("Heading: " + heading);
            }
        }
    };

    public void init() {
        super.init();

        dim = hardwareMap.deviceInterfaceModule.get("dim");
        controller = (I2cController) dim;

        if (controller != null) {
            if (DEBUG) RobotLog.d("Connection Info: " + controller.getConnectionInfo());
        } else {
            if (DEBUG) RobotLog.d("Device failed to initialize");
        }

        writeRegisterSync(OPR_MODE_ADDRESS, OPR_MODE_DATA);
        writeRegisterSync(UNIT_SEL_ADDRESS, UNIT_SEL_DATA);
    }

    @Override
    public void loop() {
        super.loop();
        
        if (loopTime == 0) {
            loopTime = System.nanoTime();
        } else {
            deltaTime = System.nanoTime() - loopTime;
            loopTime = System.nanoTime();
        }

        if (lastReadAddress != -1 && controller.isI2cPortReady(I2C_PORT) && !controller.isI2cPortActionFlagSet(I2C_PORT)) {
            controller.readI2cCacheFromController(I2C_PORT);
            Lock readLock = controller.getI2cReadCacheLock(I2C_PORT);
            try {
                readLock.lockInterruptibly();
                byte[] readCache = controller.getI2cReadCache(I2C_PORT);
                byte[] result = Arrays.copyOfRange(readCache,
                        I2cController.I2C_BUFFER_START_ADDRESS,
                        I2cController.I2C_BUFFER_START_ADDRESS + lastReadLength);
                lastReadCallback.onReadFinished(lastReadAddress, result, lastReadLength);
                resetRead();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                readLock.unlock();
            }
        }

        telemetry.addData("Heading", lastHeading);
        telemetry.addData("Time", deltaTime / Math.pow(10, 9) + "s");

        if (lastReadAddress == -1) readRegister(EUL_DATA_X_ADDRESS, 2, callback);
    }

    public void writeRegisterSync(int address, byte b) {
        waitForReady();
        if (DEBUG) RobotLog.d("I2C port is ready");

        controller.enableI2cWriteMode(I2C_PORT, I2C_ADDRESS, address, 1);

        Lock lock = controller.getI2cWriteCacheLock(I2C_PORT);
        try {
            lock.lock();
            byte[] cache = controller.getI2cWriteCache(I2C_PORT);
            cache[I2cController.I2C_BUFFER_START_ADDRESS] = b;
            if (DEBUG) RobotLog.d("Write: " + Helper.byteArrayToString(cache));
        } finally {
            lock.unlock();
        }

        controller.setI2cPortActionFlag(I2C_PORT);
        controller.writeI2cCacheToController(I2C_PORT);

        Helper.wait(250);
    }

    public void readRegister(int address, int length, I2cReadCallback cb) {
        waitForReady();

        if (DEBUG) RobotLog.d("I2C port is ready");

        controller.enableI2cReadMode(I2C_PORT, I2C_ADDRESS, address, length);

        controller.setI2cPortActionFlag(I2C_PORT);
        controller.writeI2cCacheToController(I2C_PORT);

        lastReadAddress = address;
        lastReadLength = length;
        lastReadCallback = cb;
    }

    public void waitForReady() {
        while(!controller.isI2cPortReady(I2C_PORT)) {
            Helper.wait(100);
        }
    }

    public void resetRead() {
        lastReadAddress = -1;
        lastReadLength = 0;
        lastReadCallback = null;
    }

}
