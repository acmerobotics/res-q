package com.acmerobotics.library.i2c;

import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.I2cDevice;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;

public class I2cDeviceReaderAndWriter {

    public final int msPortReadyDelay = 1;

    private final DeviceInterfaceModule dim;

    private Lock readCacheLock;
    private Lock writeCacheLock;
    private byte[] readCache;
    private byte[] writeCache;

    private final int port;
    private int address;

    public I2cDeviceReaderAndWriter(DeviceInterfaceModule dim, int address, int port) {
        this.dim = dim;
        this.port = port;
        this.address = address;

        readCache = dim.getI2cReadCache(port);
        writeCache = dim.getI2cWriteCache(port);
        readCacheLock = dim.getI2cReadCacheLock(port);
        writeCacheLock = dim.getI2cWriteCacheLock(port);
    }

    public synchronized void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            // TODO exception handling
        }
    }

    public synchronized void waitForReady() {
        while (!dim.isI2cPortReady(port)) {
            delay(msPortReadyDelay);
        }
    }

    public byte[] read(int start, int len) {
        waitForReady();
        synchronized(readCacheLock) {
            dim.enableI2cReadMode(port, address, start, len);
            dim.writeI2cCacheToController(port);
            waitForReady();
            return Arrays.copyOfRange(readCache, 0, len);
        }
    }

    public void write(int start, byte[] data, int len) {
        waitForReady();
        synchronized (writeCacheLock) {
            dim.enableI2cWriteMode(port, address, start, len);
            dim.copyBufferIntoWriteBuffer(port, data);
            dim.writeI2cCacheToController(port);
            waitForReady();
        }
    }

    public byte read8(int register) {
        return this.read(register, 1)[0];
    }

    public void write8(int register, byte data) {
        this.write(register, new byte[]{data}, 1);
    }
}
