package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.hardware.RobotController;
import com.qualcomm.ftcrobotcontroller.util.Helper;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.util.RobotLog;

import java.util.concurrent.locks.Lock;

/**
 * Created by Ryan on 11/23/2015.
 */
public class I2CTest extends RobotController {

    public static final int I2C_PORT = 0;
    public static final int I2C_ADDRESS = 0x28 * 2;
    public static final int OPR_MODE_ADDRESS = 0x3d;
    public static final byte OPR_MODE_DATA = 8;
    public static final int UNIT_SEL_ADDRESS = 0x80;
    public static final byte UNIT_SEL_DATA = 0;
    public static final int EUL_DATA_X_ADDRESS = 0x1a;

    public DeviceInterfaceModule dim;
    public I2cController controller;

    public void init() {
        super.init();

        dim = hardwareMap.deviceInterfaceModule.get("dim");
        controller = (I2cController) dim;

        if (controller != null) {
            RobotLog.d("Connection Info: " + controller.getConnectionInfo());
        } else {
            RobotLog.d("Device failed to initialize");
        }

        writeRegister(OPR_MODE_ADDRESS, OPR_MODE_DATA);
        writeRegister(UNIT_SEL_ADDRESS, UNIT_SEL_DATA);

        byte lsb, msb;
        int val;
        double heading;
        while (true) {
            byte[] arr = readRegister(EUL_DATA_X_ADDRESS, 2);
            lsb = arr[0];
            msb = arr[1];
            val =  ((int) lsb) & (((int) msb) << 8);
            heading = ((double) val) / 16.0;
            telemetry.addData("Heading", heading);
            RobotLog.d("Heading: " + heading);
            //wait(250);
        }
    }

    public void writeRegister(int address, byte b) {
        waitForReady();
        RobotLog.d("I2C port is ready");

        controller.enableI2cWriteMode(I2C_PORT, I2C_ADDRESS, address, 1);

        Lock lock = controller.getI2cWriteCacheLock(I2C_PORT);
        try {
            lock.lock();
            byte[] cache = controller.getI2cWriteCache(I2C_PORT);
            cache[I2cController.I2C_BUFFER_START_ADDRESS] = b;
            RobotLog.d("Write: " + Helper.byteArrayToString(cache));
        } finally {
            lock.unlock();
        }

        controller.setI2cPortActionFlag(I2C_PORT);
        controller.writeI2cCacheToController(I2C_PORT);

        wait(250);
    }

    public byte[] readRegister(int address, int length) {
        waitForReady();

        RobotLog.d("I2C port is ready");

        controller.enableI2cReadMode(I2C_PORT, I2C_ADDRESS, address, length);

        controller.setI2cPortActionFlag(I2C_PORT);
        controller.writeI2cCacheToController(I2C_PORT);

        wait(250);

        controller.readI2cCacheFromController(I2C_PORT);

        wait(1000);

        Lock lock = controller.getI2cReadCacheLock(I2C_PORT);
        try {
            lock.lock();
            byte[] cache = controller.getI2cReadCache(I2C_PORT);
            RobotLog.d("Read: " + Helper.byteArrayToString(cache));
            byte[] ret = new byte[length];
            for (int i = 0; i < length; i++) {
                ret[i] = cache[I2cController.I2C_BUFFER_START_ADDRESS + i];
            }
            return ret;
        } finally {
            lock.unlock();
        }
    }

    public void waitForReady() {
        while (!controller.isI2cPortReady(I2C_PORT)) {
            wait(250);
        }
    }

}
